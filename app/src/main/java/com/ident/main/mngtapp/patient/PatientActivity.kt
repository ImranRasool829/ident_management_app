package com.ident.main.mngtapp.patient

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.RecyclerCallback
import com.ident.main.mngtapp.adapter.RecyclerViewGenricAdapter
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityPatientBinding
import com.ident.main.mngtapp.databinding.ItemPatientLayoutBinding
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.PatientDataModel
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.prescription.PrescriptionContainerActivity
import com.ident.main.mngtapp.utils.SharedPrefClass
import retrofit2.Response
import java.util.Random

class PatientActivity : BaseActivity<ActivityPatientBinding>(ActivityPatientBinding::inflate) {

    private var isFBVisible: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {

            llToolBar.imgBack.setOnClickListener {
                finish()
            }
            llToolBar.tvTitle.text = resources.getString(R.string.patient_list)

            fab.setOnClickListener {
                startActivity(Intent(this@PatientActivity, AddPatientActivity::class.java))

            }

            if (isOnline(this@PatientActivity)) {
                val doctorId = SharedPrefClass().getPrefValue(
                    this@PatientActivity, GlobalAppConstant.USERID
                ).toString()
                if (doctorId != null && doctorId.isNotEmpty()) {
                    callAPI(doctorId.toInt())
                }

            } else {
                showToast(resources.getString(R.string.please_connect_with_internet))
            }

        }

    }

    private fun callAPI(body: Int) {
        showLoader()
        ApiCall.getPatientAPI(body, object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                hideLoader()
                if (mResponse.body()!!.success) {

                    viewDataBinding?.apply {
                        val patientList = mResponse.body()!!.data;
                        if (patientList.size > 0) {
                            setPatientAdapter(patientRV, patientList)
                        } else {
                            showToast(getString(R.string.data_not_found))
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                hideLoader()
                showToast(getString(R.string.data_not_found))
            }

            override fun onError(msg: Boolean) {
                hideLoader()
                showToast(resources.getString(R.string.something_went_wrong))
            }
        })

    }

    private fun setPatientAdapter(
        recyclerView: RecyclerView,
        patientList: ArrayList<PatientDataModel>
    ) {

        val rvAdapProgress =
            RecyclerViewGenricAdapter<PatientDataModel, ItemPatientLayoutBinding>(patientList,
                R.layout.item_patient_layout,
                object : RecyclerCallback<ItemPatientLayoutBinding, PatientDataModel> {
                    override fun bindData(
                        binder: ItemPatientLayoutBinding,
                        model: PatientDataModel,
                        position: Int,
                        itemView: View
                    ) {

                        binder.apply {

                            val rnd = Random()
                            val color: Int = Color.argb(
                                255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)
                            )

//                        mCardView.setCardBackgroundColor(color)
                            tvNamePT.text = model.name
                            tvMobileNum.text = model.phone
                            tvGender.text = model.gender
                            setImageWithUrl(model.profile_pics, imgPTNPic)

                            pBtnMenu.setOnClickListener {
                                showPopupMenu(pBtnMenu, position, model)
                            }
                        }
                    }
                })
        recyclerView.adapter = rvAdapProgress

    }

    private fun showPopupMenu(pBtnMenu: LinearLayout, position: Int, model: PatientDataModel) {
        val popupMenu = PopupMenu(this, pBtnMenu)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem ->

            when (item.itemId) {

                R.id.itemPD -> {
                    startActivity(
                        Intent(this@PatientActivity, PatientContainerActivity::class.java)
                            .putExtra(GlobalAppConstant.patientDetail, Gson().toJson(model))
                    )

                }

                R.id.itemEditPD -> {
                    startActivity(
                        Intent(this@PatientActivity, PatientContainerActivity::class.java)
                            .putExtra(GlobalAppConstant.patientEdit, Gson().toJson(model))
                    )
                }

                R.id.itemPatientProfile -> {
                    startActivity(
                        Intent(this@PatientActivity, PatientContainerActivity::class.java)
                            .putExtra(GlobalAppConstant.patientID, model.id.toString())
                    )
                }

                R.id.itemViewPD -> {
                    startActivity(Intent(this@PatientActivity,PrescriptionContainerActivity::class.java)
                        .putExtra(GlobalAppConstant.prescriptionDetail,"PrescriptionD"))
                }
            }

            true
        })

    }

    private fun showViewPDialog(message: String?) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.medi_dose_dura, null)
        builder.setView(dialogLayout)
        val dialog = builder.create()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = 900 // width in pixels
        lp.height = 900 // height in pixels
        lp.gravity = Gravity.CENTER
        dialog.window?.attributes = lp
        dialog.show()
        val tvTitle: TextView = dialog.findViewById(R.id.tvPrescription)
        tvTitle.text = message

    }


}

