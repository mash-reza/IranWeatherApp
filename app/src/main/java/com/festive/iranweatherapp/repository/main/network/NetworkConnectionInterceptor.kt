package com.festive.iranweatherapp.repository.main.network

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkConnectionInterceptor (val context: Context) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
        return if (isConnected()) chain.proceed(chain.request().newBuilder().build())
        else throw NoNetworkConnectivityException()
    }

    private fun isConnected(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}