package com.ident.main.mngtapp.dashboard


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.ArraySpinnerAdapter
import com.ident.main.mngtapp.adapter.RecyclerCallback
import com.ident.main.mngtapp.adapter.RecyclerViewGenricAdapter
import com.ident.main.mngtapp.appointment.AppointmentContainerActivity
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.*
import com.ident.main.mngtapp.model.AppointmentChangeStatus
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.PatientDataModel
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.utils.SharedPrefClass
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalenderActivity : BaseActivity<ActivityCalenderBinding>(ActivityCalenderBinding::inflate) {

    private var currentDate: String = ""
    private var calenderDate: String = ""
    private val requestCall = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {

            toolBar.imgBack.setOnClickListener { finish() }
            toolBar.tvTitle.text = resources.getString(R.string.calender)

            val doctorId = SharedPrefClass().getPrefValue(
                this@CalenderActivity, GlobalAppConstant.USERID
            ).toString()

            if (isOnline(this@CalenderActivity)) {
                callAPI()
            } else {
                showToast(resources.getString(R.string.please_connect_with_internet))
            }

            currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                calenderDate = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)
                if (isOnline(this@CalenderActivity)) {
                    if (doctorId != "") {
                        callAPI2(doctorId.toInt(), calenderDate, calenderDate)
                    }
                } else {
                    showToast(resources.getString(R.string.please_connect_with_internet))
                }
            }
        }
    }

    private fun callAPI() {
        showLoader()
        ApiCall.getDoctorListAPI(object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                hideLoader()
                if (mResponse.body()!!.success) {

                    viewDataBinding?.apply {
                        val doctorList = mResponse.body()!!.data
                        if (doctorList.size > 0) {
                            setSpinner(doctorList)
                            //    doctorID = doctorList[0].id.toString()
                            //  callAPI1(doctorID.toInt(), currentDate, currentDate)
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
                showToast(getString(R.string.something_went_wrong))
            }
        })

    }

    private fun callAPI1(doctorId: Int) {
        showLoader()
        ApiCall.getAppointmentListAPI(doctorId,
            object : ResponseListener<GenericModel> {
                override fun onSuccess(mResponse: Response<GenericModel>) {
                    hideLoader()
                    if (mResponse.body()!!.success) {

                        viewDataBinding?.apply {
                            val appointmentList = mResponse.body()!!.data
                            if (appointmentList.size > 0) {
                                setAdapter(calepatientRvv, appointmentList)
                            } else {
                                calepatientRvv.visibility = View.GONE
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

    private fun callAPI2(doctorId: Int, fromDate: String, toDate: String) {
        showLoader()
        ApiCall.getFilterAppointmentAPI(doctorId, fromDate, toDate,
            object : ResponseListener<GenericModel> {
                override fun onSuccess(mResponse: Response<GenericModel>) {
                    hideLoader()
                    if (mResponse.body()!!.success) {

                        viewDataBinding?.apply {
                            val appointmentList = mResponse.body()!!.data
                            if (appointmentList.size > 0) {
                                setAdapter(calepatientRvv, appointmentList)
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

    private fun callAPI3(bod: AppointmentChangeStatus) {
        showLoader()
        ApiCall.getAppointmentChangeStatusPI(bod,
            object : ResponseListener<GenericModel> {
                override fun onSuccess(mResponse: Response<GenericModel>) {
                    hideLoader()
                    if (mResponse.body()!!.success) {

                        viewDataBinding?.apply {
                            val appointmentList = mResponse.body()!!.data
                            if (appointmentList.size > 0) {
                                setAdapter(calepatientRvv, appointmentList)
                            } else {
                                calepatientRvv.visibility = View.GONE
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


    private fun setSpinner(doctorList: ArrayList<PatientDataModel>) {
        val arrayAdapter = ArraySpinnerAdapter(this@CalenderActivity,
            doctorList,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, PatientDataModel> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: PatientDataModel
                ) {
                    // doctorID = model.id.toString()
                    binder?.textSpinner?.text = model.name
                }

            }
        )

        viewDataBinding?.apply {

            edtSelectDoctor.setOnClickListener {
                mvSpinnerDoctor.performClick()
            }
            mvSpinnerDoctor.adapter = arrayAdapter
            mvSpinnerDoctor.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtSelectDoctor.setText(doctorList[p2].name)
                    val doctorId = doctorList[p2].id
                    if (doctorId != 0) {
                        callAPI1(doctorId)
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setAdapter(
        recyclerView: RecyclerView, appointmentList: ArrayList<PatientDataModel>
    ) {
        val rvAdapProgress =
            RecyclerViewGenricAdapter<PatientDataModel, ItemAppointmentListLayoutBinding>(
                appointmentList,
                R.layout.item_appointment_list_layout,
                object : RecyclerCallback<ItemAppointmentListLayoutBinding, PatientDataModel> {
                    @SuppressLint("SetTextI18n")
                    override fun bindData(
                        binder: ItemAppointmentListLayoutBinding,
                        model: PatientDataModel,
                        position: Int,
                        itemView: View
                    ) {

                        binder.apply {

//                        mCardView.setCardBackgroundColor(color)
                            tvTimePA.text = model.from_time + " - " + model.to_time
                            tvDatePA.text = model.date_of_appointment
                            tvNamePA.text = model.name
                           // tvDoctorNamePA.text = model.description
                            tvStatusPA.text = model.status

                            mllPopUpAppointment.setOnClickListener {
                                showPopupMenu(mllPopUpAppointment, model)
                            }

                        }
                    }
                })
        recyclerView.adapter = rvAdapProgress

    }

    private fun showPopupMenu(pBtnMenu: LinearLayout, model: PatientDataModel) {
        val popupMenu = PopupMenu(this, pBtnMenu)
        popupMenu.inflate(R.menu.popup_menu_appointment)
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem ->

            when (item.itemId) {

                R.id.itemPAEditAppointment -> {
                    val data: String = Gson().toJson(model)

                    startActivity(
                        Intent(
                            this@CalenderActivity,
                            AppointmentContainerActivity::class.java
                        ).putExtra(GlobalAppConstant.editAppointment, "EDIT")
                            .putExtra(GlobalAppConstant.appointmentData, data)
                    )
                }

                //approved, cancelled, pending
                R.id.itemDeletePA -> {
                    callAPI3(AppointmentChangeStatus("cancelled", model.id.toString()))
                }

                R.id.itemNoShow -> {
                    callAPI3(AppointmentChangeStatus("pending", model.id.toString()))
                }

                R.id.itemCallPA -> {
                    if (model.phone != "") {
                        call(model.phone)
                    }
                }

            }

            true
        })

    }

    private fun call(phone: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phone")
        startActivity(dialIntent)
    }

    /*
        private fun makePhoneCall() {
            val number: String = "9958483145"
            if (number.trim { it <= ' ' }.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(
                        this@CalenderActivity,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@CalenderActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestCall
                    )
                } else {
                    val dial = "tel:$number"
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                }
            } else {
                Toast.makeText(this@CalenderActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if (requestCode == requestCall) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall()
                } else {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
                }
            }
        }*/
}