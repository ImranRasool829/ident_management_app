package com.ident.main.mngtapp.network

import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File


object ApiCall {

    fun loginAPI(
        body: LoginModelP, callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().login(body)
        )

    }

    fun getUserAPI(callback: ResponseListener<GenericModel>) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getUser()
        )

    }

    fun getDashBoardPatientDetailAPI(
        doctorId: Int,
        callback: ResponseListener<GenericModel1>
    ) {
        val mApiService = ApiService<GenericModel1>()
        mApiService.get(
            object : ApiResponse<GenericModel1> {
                override fun onResponse(mResponse: Response<GenericModel1>) {
                    checkCode1(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel1>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getDashboardPatientDetail(doctorId)
        )

    }

    fun getDashBoardPatientFilterAPI(
        doctorId: Int,
        fromDate: String,
        toDate: String,
        callback: ResponseListener<GenericModel1>
    ) {
        val mApiService = ApiService<GenericModel1>()
        mApiService.get(
            object : ApiResponse<GenericModel1> {
                override fun onResponse(mResponse: Response<GenericModel1>) {
                    checkCode1(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel1>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getDashboardPatientFilter(doctorId, fromDate, toDate)
        )

    }


    fun getPatientAPI(
        doctorId: Int,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getPatients(doctorId)
        )

    }

    fun getPatientDetailAPI(
        patientId: String,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getPatientDetail(patientId)
        )

    }

    fun getAppointmentListAPI(
        doctorId: Int,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getAppointmentList(doctorId)
        )

    }

    fun getFilterAppointmentAPI(
        doctorId: Int,
        fromDate: String,
        toDate: String,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getFilterAppointments(doctorId, fromDate, toDate)
        )

    }

    fun getUpdateProfileAPI(
        body: ProfileUpdateModelP,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getUpdateProfile(body)
        )

    }

    fun getAddPatientDAPI(
        body: AddPatientPD,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getAddPatient(body)
        )

    }

    fun getAddPPrescriptionAPI(
        body: PrescriptionModelP,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getAddPrescription(body)
        )

    }

    fun getDoctorListAPI(
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getDoctorList()
        )

    }

    fun getDepartmentAPI(
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getDepartment()
        )

    }


    fun getServiceAPI(
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getService()
        )

    }

    fun getAddAppointmentPI(
        body: AddAppointmentMParam,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getAddAppointment(body)
        )

    }

    fun getUpdateAppointmentPI(
        body: UpdateAppointmentMParam,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getEditAppointment(body)
        )

    }

    fun getAppointmentChangeStatusPI(
        body: AppointmentChangeStatus,
        callback: ResponseListener<GenericModel>
    ) {
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface().getAppointmentChangeStatus(body)
        )

    }

    fun uploadImg(
        imageFile: String,
        callback: ResponseListener<GenericModel>
    ) {
        val hashMap = HashMap<String, RequestBody>()
        val userImage = File(imageFile)
        val body = userImage.asRequestBody("image/*".toMediaTypeOrNull())
        hashMap["file\"; filename=\"" + userImage.name] = body
        val mApiService = ApiService<GenericModel>()
        mApiService.get(
            object : ApiResponse<GenericModel> {
                override fun onResponse(mResponse: Response<GenericModel>) {
                    checkCode(mResponse, callback)
                }

                override fun onError(mKey: Call<GenericModel>?, t: Throwable?, msg: String) {
                    callback.onError(msg)
                }
            },
            ApiClient.getApiInterface()
                .imgUploadApi(hashMap)
        )

    }

    fun checkCode(
        mResponse: Response<GenericModel>,
        callback: ResponseListener<GenericModel>
    ) {
        if (mResponse.code() == 200) {
            callback.onSuccess(mResponse)
        } else if (mResponse.code() == 401) {
            // homeActivity.logout()
            BaseActivity.showToast("Token is Expired")

        } else {
            try {
                val jObjError = mResponse.errorBody()?.string()?.let { JSONObject(it) }
                BaseActivity.showToast(jObjError!!.getString("responseMessage"))

                if (jObjError != null) {
                    if (jObjError.has("result")) {
                        val jsonObject = jObjError.getJSONObject("result")

                        if (jsonObject.has("isVerified"))
                            if (jsonObject.getBoolean("isVerified")) {
                                callback.onError(true)
                            } else {
                                if (jsonObject.has("_id"))
                                //SharedPrefClass().putObject(homeActivity,
                                //  GlobalAppConstant.USERID,
                                //jsonObject.getString("_id")
                                //)
                                    callback.onError(false)
                            }

                    }
                }
            } catch (e: Exception) {
                callback.onError(false)
            }

        }
    }

    fun checkCode1(
        mResponse: Response<GenericModel1>,
        callback: ResponseListener<GenericModel1>
    ) {
        if (mResponse.code() == 200) {
            //  homeActivity.showToast(mResponse.body()?.responseMessage)
            callback.onSuccess(mResponse)
        } else {
            val jObjError = mResponse.errorBody()?.string()?.let { JSONObject(it) }
            //showToast(jObjError?.getString("responseMessage"))
            BaseActivity.showToast("Token is Expired")
            if (jObjError != null) {
                if (jObjError.has("status")) {
                    val jsonObject = jObjError.getJSONObject("status")
                    if (jsonObject.has("isVerified"))
                        if (jsonObject.getBoolean("isVerified")) {
                            callback.onError(true)
                        } else {
                            callback.onError(false)
                        }

                }
            }

        }
    }

}

interface ResponseListener<T> {
    fun onSuccess(mResponse: Response<T>)
    fun onError(msg: String)
    fun onError(msg: Boolean) {
    }
}

