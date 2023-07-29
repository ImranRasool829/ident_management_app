package com.ident.main.mngtapp.network

import com.ident.main.mngtapp.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface API {

    @POST("api/login")
    fun login(@Body body: LoginModelP): Call<GenericModel>

    @POST("api/get_user")
    fun getUser(): Call<GenericModel>

    @Multipart
    @POST("common/getUrl")
    fun imgUploadApi(@PartMap hashMap: HashMap<String, RequestBody>): Call<GenericModel>

    //patients?doctor_id=13
    @GET("api/patients")
    fun getPatients(
        @Query("doctor_id") doctorID: Int
    ): Call<GenericModel>

    @GET("api/patient-details/{id}")
    fun getPatientDetail(@Path("id") id: String): Call<GenericModel>

    @GET("api/dashboard")
    fun getDashboardPatientFilter(
        @Query("doctor_id") doctorID: Int,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String
    ): Call<GenericModel1>

    @GET("api/dashboard")
    fun getDashboardPatientDetail(
        @Query("doctor_id") doctorID: Int
    ): Call<GenericModel1>

    @GET("api/appointments")
    fun getAppointmentList(@Query("doctor_id") doctorID: Int): Call<GenericModel>

    //2023-04-07 yyyy-mm-dd
    @GET("api/appointments")
    fun getFilterAppointments(
        @Query("doctor_id") doctorID: Int,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String
    ): Call<GenericModel>

    @POST("api/update-profile")
    fun getUpdateProfile(@Body body: ProfileUpdateModelP): Call<GenericModel>

    @POST("api/add-patient")
    fun getAddPatient(@Body body: AddPatientPD): Call<GenericModel>

    @POST("api/add-prescription")
    fun getAddPrescription(@Body body: PrescriptionModelP): Call<GenericModel>

    @GET("api/doctorlist")
    fun getDoctorList(): Call<GenericModel>

    @GET("api/department")
    fun getDepartment(): Call<GenericModel>


    @GET("api/service")
    fun getService(): Call<GenericModel>

    @POST("api/add-appointment")
    fun getAddAppointment(@Body body:AddAppointmentMParam): Call<GenericModel>

    @POST("api/update-appointment")
    fun getEditAppointment(@Body body:UpdateAppointmentMParam): Call<GenericModel>

    @POST("api/appointment-change-status")
    fun getAppointmentChangeStatus(@Body body:AppointmentChangeStatus): Call<GenericModel>


}