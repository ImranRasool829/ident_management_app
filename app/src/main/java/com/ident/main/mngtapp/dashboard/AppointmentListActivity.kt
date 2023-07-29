package com.ident.main.mngtapp.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.RecyclerCallback
import com.ident.main.mngtapp.adapter.RecyclerViewGenricAdapter
import com.ident.main.mngtapp.appointment.AppointmentContainerActivity
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityAppointmentListBinding
import com.ident.main.mngtapp.databinding.ItemAppointmentListLayoutBinding
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.PatientDataModel
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.utils.SharedPrefClass
import retrofit2.Response
import kotlin.collections.ArrayList

class AppointmentListActivity :
    BaseActivity<ActivityAppointmentListBinding>(ActivityAppointmentListBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {

            llToolBar.imgBack.setOnClickListener {
                finish()
            }
            llToolBar.tvTitle.text = resources.getString(R.string.appointment_list)
        }

        if (isOnline(this@AppointmentListActivity)) {
            val doctorId = SharedPrefClass().getPrefValue(
                this@AppointmentListActivity, GlobalAppConstant.USERID
            ).toString()
            if (doctorId != "") {
                callAPI(doctorId.toInt())
            }
            //callAPI(13, "2023-04-07", "2023-04-09")
        } else {
            showToast(resources.getString(R.string.please_connect_with_internet))
        }


    }

    private fun callAPI(doctorId: Int) {
        showLoader()
        ApiCall.getAppointmentListAPI(doctorId,
            object : ResponseListener<GenericModel> {
                override fun onSuccess(mResponse: Response<GenericModel>) {
                    hideLoader()
                    if (mResponse.body()!!.success) {

                        viewDataBinding?.apply {
                            val appointmentList = mResponse.body()!!.data
                            if (appointmentList.size > 0) {
                                setAppointmentRVA(mRVAppointmentP, appointmentList)
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

    private fun callAPI1(doctorId: Int, fromDate: String, toDate: String) {
        showLoader()
        ApiCall.getFilterAppointmentAPI(
            doctorId,
            fromDate,
            toDate,
            object : ResponseListener<GenericModel> {
                override fun onSuccess(mResponse: Response<GenericModel>) {
                    hideLoader()
                    if (mResponse.body()!!.success) {

                        viewDataBinding?.apply {
                            val appointmentList = mResponse.body()!!.data
                            if (appointmentList.size > 0) {
                                setAppointmentRVA(mRVAppointmentP, appointmentList)
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

    private fun setAppointmentRVA(
        recyclerView: RecyclerView,
        appointmentList: ArrayList<PatientDataModel>
    ) {

        val rvAdapProgress =
            RecyclerViewGenricAdapter<PatientDataModel, ItemAppointmentListLayoutBinding>(
                appointmentList,
                R.layout.item_appointment_list_layout,
                object : RecyclerCallback<ItemAppointmentListLayoutBinding, PatientDataModel> {
                    override fun bindData(
                        binder: ItemAppointmentListLayoutBinding,
                        model: PatientDataModel,
                        position: Int,
                        itemView: View
                    ) {

                        binder.apply {

//                        mCardView.setCardBackgroundColor(color)
                            tvDatePA.text = model.date_of_appointment
                            tvNamePA.text = model.name
                            tvStatusPA.text = model.status

                            mllPopUpAppointment.setOnClickListener {
                                showPopupMenu(mllPopUpAppointment)
                            }

                        }
                    }
                })
        recyclerView.adapter = rvAdapProgress

    }

    private fun showPopupMenu(pBtnMenu: LinearLayout) {
        val popupMenu = PopupMenu(this, pBtnMenu)
        popupMenu.inflate(R.menu.popup_menu_appointment)
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem ->

            when (item.itemId) {

                R.id.itemDeletePA -> {
                    Toast.makeText(
                        this@AppointmentListActivity,
                        "work is going on..",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                R.id.itemPAEditAppointment -> {
                    startActivity(
                        Intent(
                            this@AppointmentListActivity,
                            AppointmentContainerActivity::class.java
                        )
                    )
                }
/*
                R.id.itemDeletePA -> {
                    deleteCustomDialog()
                }*/
            }

            true
        })

    }


}