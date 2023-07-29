package com.ident.main.mngtapp.network

import androidx.annotation.NonNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ApiService<T> {
    fun get(mApiResponse: ApiResponse<T>, methodName: Call<T>) {
        methodName.enqueue(object : Callback<T> {

            override fun onResponse(@NonNull call: Call<T>, @NonNull response: Response<T>) {
                //  if (response.code() == 400) {

                // } else if (response.code() == 200) {
                mApiResponse.onResponse(response)
                //   }
            }

            override fun onFailure(@NonNull call: Call<T>, @NonNull t: Throwable) {
                if (t is SocketTimeoutException) {
                    mApiResponse.onError(call, t, "Your internet speed is slow, please check it.")
                } else {
                    mApiResponse.onError(call, t)
                }
            }
        })
    }


}
