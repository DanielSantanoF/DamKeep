package com.dsantano.damkeepapp.api.generators

import android.content.Context
import com.dsantano.damkeepapp.common.Constants
import com.dsantano.damkeepapp.common.MyApp
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val authToken = MyApp.instance.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).getString(Constants.AUTH_TOKEN, null);

        var request = chain.request()

        request = request?.newBuilder()
            .addHeader("Authorization", "Bearer ${authToken}")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        return chain.proceed(request)

    }
}