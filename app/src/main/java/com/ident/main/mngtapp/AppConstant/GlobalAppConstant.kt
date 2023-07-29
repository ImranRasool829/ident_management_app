package com.ident.main.mngtapp.AppConstant

import com.ident.main.mngtapp.IDentApplication
import com.ident.main.mngtapp.R

object GlobalAppConstant {

    @JvmStatic
    var APP_VERSION = "1.0"

    @JvmStatic
    val SHARED_PREF = IDentApplication.instance.resources.getString(R.string.shared_pref)

    const val EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"


    //SharedPref Key
    @JvmStatic
    val USERID = "user_id"

    @JvmStatic
    val ACCESS_TOKEN = "accessToken"

    @JvmStatic
    val USER_DATA = "userData"

    @JvmStatic
    val IS_LOGIN = "isLogin"

    //Intent Key
    const val aboutUS = "aboutUs"
    const val changePassword = "changePassword"
    const val feedback = "feedback"
    const val patientDetail = "patientDetail"
    const val patientEdit = "patientEdit"
    const val prescriptionDetail = "prescriptionDetail"
    const val addPrescription = "addPrescription"
    const val patientID = "patientID"
    const val billingHistory = "billingHistory"
    const val editAppointment = "editAppointment"
    const val appointmentData = "appointmentData"

}