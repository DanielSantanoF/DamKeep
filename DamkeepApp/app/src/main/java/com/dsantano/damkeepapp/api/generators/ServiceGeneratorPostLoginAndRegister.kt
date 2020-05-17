package com.dsantano.damkeepapp.api.generators

import com.dsantano.damkeepapp.api.ApiService
import com.dsantano.damkeepapp.common.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ServiceGeneratorPostLoginAndRegister {

    private val apiService: ApiService
    private val retrofit: Retrofit

    init {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(interceptor)

        val cliente = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(cliente)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getLoginAndRegisterService() = apiService

}