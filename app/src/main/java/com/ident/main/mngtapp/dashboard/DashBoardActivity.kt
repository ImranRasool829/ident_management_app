package com.ident.main.mngtapp.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.RecyclerCallback
import com.ident.main.mngtapp.adapter.RecyclerViewGenricAdapter
import com.ident.main.mngtapp.appointment.AppointmentContainerActivity
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.billing.BillingContainerActivity
import com.ident.main.mngtapp.databinding.ActivityDashBoardBinding
import com.ident.main.mngtapp.databinding.ItemMainCatLayoutBinding
import com.ident.main.mngtapp.databinding.ItemPatientCatLayoutBinding
import com.ident.main.mngtapp.model.GenericModel1
import com.ident.main.mngtapp.model.MainCatModel
import com.ident.main.mngtapp.model.UserDataM
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.patient.AddPatientActivity
import com.ident.main.mngtapp.patient.PatientActivity
import com.ident.main.mngtapp.prescription.PrescriptionContainerActivity
import com.ident.main.mngtapp.setting.SettingActivity
import com.ident.main.mngtapp.utils.SharedPrefClass
import retrofit2.Response
import java.util.*


class DashBoardActivity :
    BaseActivity<ActivityDashBoardBinding>(ActivityDashBoardBinding::inflate) {

    private var isFBVisible: Boolean = true;
    private var rvAdapProgress1: RecyclerViewGenricAdapter<MainCatModel, ItemMainCatLayoutBinding>? =
        null
    private lateinit var dataPatientDetail: UserDataM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {


            edtStartDate.setOnClickListener {
                customDateRange()
            }

            edtEndDate.setOnClickListener {
                customDateRange()
            }

            setAdapter2(mVCardView)

        }

        if (isOnline(this@DashBoardActivity)) {
            val doctorId = SharedPrefClass().getPrefValue(
                this@DashBoardActivity, GlobalAppConstant.USERID
            ).toString()
            // callDashboardPatientFilterAPI(13, "2023-04-07", "2023-04-09")
            callAPI(doctorId.toInt())
        } else {
            showToast(resources.getString(R.string.please_connect_with_internet))
        }

        setNavItem()
        setFunctionFB()
        updateUI()
        filterDashboardItem()
    }

    private fun setNavItem() {

        viewDataBinding?.apply {

            llToolBar.imgMenu.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            navLayout.navLogout.setOnClickListener {
                logoutDialog()
            }

            navLayout.navshare.setOnClickListener {
                shareAppLink()
            }

            navLayout.navChangePasswordTV.setOnClickListener {
                startActivity(
                    Intent(this@DashBoardActivity, SettingActivity::class.java).putExtra(
                        GlobalAppConstant.changePassword,
                        "Change Password"
                    )
                )

            }
            navLayout.navaboutas.setOnClickListener {
                startActivity(
                    Intent(
                        this@DashBoardActivity, SettingActivity::class.java
                    ).putExtra(GlobalAppConstant.aboutUS, "aboutUs")
                )

            }
            navLayout.sendfeedback.setOnClickListener {
                startActivity(
                    Intent(
                        this@DashBoardActivity,
                        SettingActivity::class.java
                    ).putExtra(GlobalAppConstant.feedback, "feedback")
                )

            }


            llAppointment.setOnClickListener {
                startActivity(Intent(this@DashBoardActivity, AppointmentContainerActivity::class.java))
            }

            llAddPatient.setOnClickListener {
                startActivity(Intent(this@DashBoardActivity, AddPatientActivity::class.java))
            }

            llBilling.setOnClickListener {
                startActivity(Intent(this@DashBoardActivity, BillingContainerActivity::class.java))
            }
        }
    }

    private fun filterDashboardItem() {

        viewDataBinding?.apply {
            llToolBar.edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s != null && s.isNotEmpty()) {
                        rvAdapProgress1?.filter?.filter(s.toString())
                        mVCardView.visibility = View.VISIBLE
                    } else {
                        rvAdapProgress1?.filter?.filter("")
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        }
    }

    private fun setFunctionFB() {

        viewDataBinding?.apply {
            mFloatBTN.setOnClickListener {
                if (isFBVisible) {
                    llAddPatient.visibility = View.VISIBLE
                    llAppointment.visibility = View.VISIBLE
                    llBilling.visibility = View.VISIBLE
                    mFloatBTN.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@DashBoardActivity, R.drawable.cross
                        )
                    )
                    isFBVisible = false
                } else {
                    llAppointment.visibility = View.GONE
                    llAddPatient.visibility = View.GONE
                    llBilling.visibility = View.GONE
                    mFloatBTN.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@DashBoardActivity, R.drawable.plus
                        )
                    )
                    isFBVisible = true;
                }
            }
        }
    }

    private fun updateUI() {
        val model: String =
            SharedPrefClass().getPrefValue(this, GlobalAppConstant.USER_DATA).toString()
        val data: UserDataM = Gson().fromJson(model, UserDataM::class.java)

        viewDataBinding?.apply {
            if (data != null) {
                navLayout.tvNameNav.text = data.name
                navLayout.tvEmailNav.text = data.email
                setImageWithUrl(data.profile_pics, navLayout.imgnav)
            }
        }
    }

    private fun setAdapter1(recyclerView: RecyclerView) {

        val list = ArrayList<String>()
        list.add("12")
        list.add("50")
        list.add("56")
        list.add("66")

        val rvAdapProgress = RecyclerViewGenricAdapter<String, ItemPatientCatLayoutBinding>(list,
            R.layout.item_patient_cat_layout,
            object : RecyclerCallback<ItemPatientCatLayoutBinding, String> {
                override fun bindData(
                    binder: ItemPatientCatLayoutBinding,
                    model: String,
                    position: Int,
                    itemView: View
                ) {

                    binder.apply {

                        val rnd = Random()
                        val color: Int = Color.argb(
                            255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)
                        )

                        mCardView.setCardBackgroundColor(color)
                        when (position) {
                            0 -> {
                                tvPatientCount.text = dataPatientDetail.totalpatient
                                tvPatientType.text = resources.getString(R.string.total_patient)
                            }

                            1 -> {
                                tvPatientCount.text = dataPatientDetail.totalapproved
                                tvPatientType.text = resources.getString(R.string.total_approved)
                            }

                            2 -> {
                                tvPatientCount.text = dataPatientDetail.totalpending
                                tvPatientType.text = resources.getString(R.string.total_pending)

                            }

                            else -> {
                                tvPatientCount.text = dataPatientDetail.totalcancelled
                                tvPatientType.text = resources.getString(R.string.total_cancelled)
                            }
                        }


                    }
                }
            })
        recyclerView.adapter = rvAdapProgress

    }

    private fun setAdapter2(recyclerView: RecyclerView) {
        val list = ArrayList<MainCatModel>()
        val cat = MainCatModel("Calendar")
        val cat1 = MainCatModel("Patient")
        val cat2 = MainCatModel("Billing")
        val cat3 = MainCatModel("Prescription")
        val cat4 = MainCatModel("Campaign")
        val cat5 = MainCatModel("Doctor")
        list.add(cat)
        list.add(cat1)
        list.add(cat2)
        list.add(cat3)
        list.add(cat4)
       // list.add(cat5)
        list.add(cat5)

        //val cat5 = MainCatModel("Billing History")


        rvAdapProgress1 = RecyclerViewGenricAdapter<MainCatModel, ItemMainCatLayoutBinding>(list,
            R.layout.item_main_cat_layout,
            object : RecyclerCallback<ItemMainCatLayoutBinding, MainCatModel> {
                override fun bindData(
                    binder: ItemMainCatLayoutBinding,
                    model: MainCatModel,
                    position: Int,
                    itemView: View
                ) {
                    binder.apply {
                        tvCatTitle.text = model.catName
                        when (position) {
                            0 -> {

                                mConstItemListener.setOnClickListener {
                                    startActivity(
                                        Intent(
                                            this@DashBoardActivity, CalenderActivity::class.java
                                        )
                                    )
                                }
                                imgItemCat.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        this@DashBoardActivity, R.drawable.ic_calendar
                                    )
                                );
                            }

                            1 -> {
                                mConstItemListener.setOnClickListener {
                                    val intent =
                                        Intent(this@DashBoardActivity, PatientActivity::class.java)
                                    startActivity(intent)
                                }
                                imgItemCat.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        this@DashBoardActivity, R.drawable.ic_patient
                                    )
                                );

                            }

                            2 -> {
                                mConstItemListener.setOnClickListener {
                                    val intent = Intent(
                                        this@DashBoardActivity, BillingContainerActivity::class.java
                                    )
                                    startActivity(intent)
                                }
                                imgItemCat.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        this@DashBoardActivity, R.drawable.ic_billing
                                    )
                                );
                            }

                            3 -> {

                                mConstItemListener.setOnClickListener {
                                    startActivity(
                                        Intent(
                                            this@DashBoardActivity,
                                            PrescriptionContainerActivity::class.java
                                        )
                                            .putExtra(
                                                GlobalAppConstant.prescriptionDetail,
                                                "PrescriptionD"
                                            )
                                    )

                                }

                                imgItemCat.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        this@DashBoardActivity, R.drawable.ic_prescription
                                    )
                                );
                            }

                            4 -> {

                                mConstItemListener.setOnClickListener {
                                    val intent =
                                        Intent(this@DashBoardActivity, CampaignActivity::class.java)
                                    startActivity(intent)
                                }

                                imgItemCat.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        this@DashBoardActivity, R.drawable.ic_compaign
                                    )
                                );
                            }
                            /*5 -> {
                                mConstItemListener.setOnClickListener {
                                    startActivity(
                                        Intent(
                                            this@DashBoardActivity,
                                            SettingActivity::class.java
                                        ).putExtra(GlobalAppConstant.billingHistory, "BHistory")
                                    )

                                }
                                imgItemCat.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        this@DashBoardActivity, R.drawable.ic_billing
                                    )
                                );
                            }*/

                            5 -> {

                                mConstItemListener.setOnClickListener {
                                    val intent =
                                        Intent(
                                            this@DashBoardActivity,
                                            SettingActivity::class.java
                                        )
                                    startActivity(intent)
                                }
                                imgItemCat.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        this@DashBoardActivity, R.drawable.doctor
                                    )
                                );
                            }
                        }


                    }
                }

                override fun getFilterChar(row: MainCatModel, s: String): Boolean {
                    if ((row.catName.lowercase(Locale.ROOT)).contains(s)) {
                        return true
                    }
                    return false
                }
            })
        viewDataBinding?.apply {
            recyclerView.adapter = rvAdapProgress1
        }
    }

    private fun callAPI(doctorId: Int) {
        showLoader()
        ApiCall.getDashBoardPatientDetailAPI(
            doctorId,
            object : ResponseListener<GenericModel1> {
                override fun onSuccess(mResponse: Response<GenericModel1>) {
                    hideLoader()
                    if (mResponse.body()!!.success) {
                        viewDataBinding?.apply {
                            dataPatientDetail = mResponse.body()!!.data;
                            setAdapter1(mHCardView)

                        }
                    } else {
                        showToast(resources.getString(R.string.data_not_found))
                    }
                }

                override fun onError(msg: String) {
                    hideLoader()
                    showToast(msg)
                }

                override fun onError(msg: Boolean) {
                    hideLoader()
                    showToast(resources.getString(R.string.something_went_wrong))
                }

            })


    }

    private fun callAPI1(doctorId: Int, fromDate: String, toDate: String) {
        showLoader()
        ApiCall.getDashBoardPatientFilterAPI(
            doctorId,
            fromDate,
            toDate,
            object : ResponseListener<GenericModel1> {
                override fun onSuccess(mResponse: Response<GenericModel1>) {
                    hideLoader()
                    if (mResponse.body()!!.success) {
                        viewDataBinding?.apply {

                            if (mResponse.body()!!.success) {
                                viewDataBinding?.apply {
                                    dataPatientDetail = mResponse.body()!!.data;
                                    setAdapter1(mHCardView)
                                }
                            } else {
                                showToast(resources.getString(R.string.data_not_found))
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

//    private fun showPrescription() {
//        val builder = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        val dialogLayout = inflater.inflate(R.layout.medi_dose_dura, null)
//        builder.setView(dialogLayout)
//        val dialog = builder.create()
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialog.window?.attributes)
//        lp.width = 900 // width in pixels
//        lp.height = 900 // height in pixels
//        lp.gravity = Gravity.CENTER
//        dialog.window?.attributes = lp
//
//        dialog.show()
//    }

    private fun customDateRange() {

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_custom_date_range_layout)
        val startDate = dialog.findViewById<EditText>(R.id.edtStartDate)
        val endDate = dialog.findViewById<EditText>(R.id.edtEndDate)
        val submitBTNDB = dialog.findViewById<Button>(R.id.submitBTNDB)
        val lp = WindowManager.LayoutParams()
        lp.width = 900 // width in pixels
        lp.height = 900 // height in pixels
        lp.gravity = Gravity.CENTER
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        startDate.setOnClickListener {
            datePicker(startDate, min = false, max = true)
        }

        endDate.setOnClickListener {
            datePicker(endDate, min = false, max = true)
        }

        submitBTNDB.setOnClickListener {
            if (startDate.text.isNullOrEmpty()) {
                showToast(getString(R.string.start_time))
            } else if (endDate.text.isNullOrEmpty()) {
                showToast(getString(R.string.select_date))
            } else {
                dialog.dismiss()
                viewDataBinding?.apply {
                    edtStartDate.setText(startDate.text.toString() + " -")
                    edtEndDate.setText(endDate.text.toString())

                    if (isOnline(this@DashBoardActivity)) {
                        val doctorId = SharedPrefClass().getPrefValue(
                            this@DashBoardActivity, GlobalAppConstant.USERID
                        ).toString()
                        // callDashboardPatientFilterAPI(13, "2023-04-07", "2023-04-09")
                        callAPI1(
                            doctorId.toInt(),
                            startDate.text.toString(),
                            endDate.text.toString()
                        )
                    } else {
                        showToast(resources.getString(R.string.please_connect_with_internet))
                    }

                }
            }


        }


    }

    private fun logoutDialog() {
        val builder = AlertDialog.Builder(this@DashBoardActivity, R.style.MyDialogTheme)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_logout, null)
        builder.setView(dialogLayout)
        val dialog = builder.create()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = 900 // width in pixels
        lp.height = 900 // height in pixels
        lp.gravity = Gravity.CENTER
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val crossTV = dialog.findViewById<TextView>(R.id.noBT)
        val doneBTNRP = dialog.findViewById<TextView>(R.id.yesBT)

        crossTV.setOnClickListener {
            dialog.dismiss()
        }

        doneBTNRP.setOnClickListener {
            logout()
        }
    }

    /*
        override fun onBackPressed() {
            *//*   if (supportFragmentManager.backStackEntryCount > 0) {
               supportFragmentManager.popBackStack()
           } else {
               finishAffinity()

           }
   *//*
    }*/

    override fun getOnBackInvokedDispatcher(

    ): OnBackInvokedDispatcher {
        return super.getOnBackInvokedDispatcher()
    }
}
