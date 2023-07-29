package com.ident.main.mngtapp.model

data class GenericModel(
    var message: String,
    var token: String,
    var status: Int,
    var success: Boolean = false,
    var data: ArrayList<PatientDataModel>,
    var user: UserDataM
)

data class GenericModel1(
    var message: String,
    var token: String,
    var status: Int,
    var success: Boolean,
    var data: UserDataM
)

data class PrescriptionModelP(
    var patient_id: String,
    var medicine: String,
    var take_time: String,
    var dose: String,
    var no_of_days: String,
    var no_of_time: String,
    var instructions: String
)

data class LoginModelP(
    var email: String,
    var password: String
)

data class ProfileUpdateModelP(
    var id: Int,
    var name: String,
    var phone: String,
    var dob: String,
    var gender: String,
    var address: String,
    var city: String,
    var state: String,
    var pincode: String,
    var status: String,
    var education: String,
    var designation: String,
    var department: String

)

data class AddPatientPD(
    var doctor_id: String,
    var name: String,
    var phone: String,
    var email: String,
    var gender: String,
    var dob: String,
    var reason_to_come: String,
    var allergies: String,
    var smoking: String,
    var drinking: String,
    var tobacco: String,
    var illness: String,
    var address: String
)

data class AddAppointmentMParam(
    var name: String,
    var phone: String,
    var doctor: String,
    var date_of_appointment: String,
    var treatment: String,
    var from_time: String,
    var to_time: String,
    var notes: String
)

data class UpdateAppointmentMParam(
    var appointment_id: String,
    var doctor: String,
    var treatment: String,
    var date_of_appointment: String,
    var from_time: String,
    var to_time: String,
    var notes: String
)

data class AppointmentChangeStatus(
    var status: String,
    var appointment_id: String
)


//response
data class UserDataM(
    var id: Int,
    var name: String,
    var email: String,
    var phone: String,
    var dob: String,
    var gender: String,
    var blood_group: String,
    var education: String,
    var designation: String,
    var department: String,
    var address: String,
    var city: String,
    var state: String,
    var pincode: String,
    var profile_pics: String,
    var email_verified_at: String,
    var status: String,
    var deleted_at: String,
    var created_at: String,
    var totalapproved: String,
    var totalcancelled: String,
    var totalpatient: String,
    var totalpending: String
)

data class MainCatModel(
    var catName: String
)

data class BillingModel(
    var patientName: String,
    var date: String,
    var medicine: String,
    var designation: String,
    var teethNumber: String,
    var price: String,
    var quantity: String
)

data class PatientDataModel(
    var address: String,
    var blood_group: String,
    var city: String,
    var dob: String,
    var email: String,
    var gender: String,
    var role: String,
    var id: Int,
    var name: String,
    var phone: String,
    var profile_pics: String,
    var status: String,
    var user_id: String,
    var from_time: String,
    var notes: String,
    var to_time: String,
    var treatment: String,
    var reason_to_come: String,
    var date_of_appointment: String,
    var department_name: String,
    var allergies: String,
    var smoking: String,
    var drinking: String,
    var tobacco: String,
    var type: String,
    var description: String,
    var price: String
)


data class ParamModel(var id: String = "", var isSelect: Boolean = false)

/*data class ToolBarModel(
    var backBtn: Boolean = false,
    var sideNav: Boolean = false,
    var bgToolBar: Int = 0,
    var toolBarShow: Boolean = false,
    var toolBarTtlShow: Boolean = false,
    var toolBarProfileShow: Boolean = false,
    var notiIconShow: Boolean = false,
    var imgGrpUserShow: Boolean = false,
    var imgAudiohow: Boolean = false,
    var imgVideoShow: Boolean = false,
    var toolBarTtl: String = ""
)*/
