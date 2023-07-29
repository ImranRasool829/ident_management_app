package com.ident.main.mngtapp.network

import retrofit2.Call
import retrofit2.Response

interface ApiResponse<T> {
    fun onResponse(mResponse: Response<T>)
    fun onError(mKey: Call<T>? = null, t: Throwable? = null, msg: String = "")
}