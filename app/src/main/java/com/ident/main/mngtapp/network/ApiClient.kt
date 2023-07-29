package com.ident.main.mngtapp.network

import android.util.Log
import androidx.annotation.NonNull
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.IDentApplication
import com.ident.main.mngtapp.utils.SharedPrefClass
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    //   var BASE_URL = "http://3.109.89.215:3000/api/v1/"
    var BASE_URL = "https://ident.theident.net.in/"


    @JvmStatic
    private var mApiInterface: API? = null

    @JvmStatic
    fun getApiInterface(): API {
        return setApiInterface()
    }

    @JvmStatic
    private fun setApiInterface(): API {
        var mAuthToken = ""
        mAuthToken = SharedPrefClass().getPrefValue(
            IDentApplication.instance,
            GlobalAppConstant.ACCESS_TOKEN
        ).toString()

        Log.d("mAuthToken", mAuthToken)


        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(5000, TimeUnit.MINUTES)
            .readTimeout(5000, TimeUnit.MINUTES)

        val mBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.interceptors().add(logging)

        val interceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(@NonNull chain: Interceptor.Chain): Response {
                val original = chain.request()
                val builder = original.newBuilder()
                if (mAuthToken.isNotEmpty())
                    builder.header("Authorization", "Bearer " + mAuthToken)
                val request = builder.build()
                val response = chain.proceed(request)
                return if (response.code == 401) {
                    response
                } else response
            }
        }

        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor)
            mBuilder.client(httpClient.build())
            mApiInterface = mBuilder.build().create(API::class.java)
        }

        return mApiInterface!!
    }


}