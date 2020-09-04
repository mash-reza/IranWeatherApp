package com.festive.iranweatherapp.repository.main.network

import java.io.IOException

class NoNetworkConnectivityException : IOException() {
    override val message: String?
        get() = "No Internet Connection"
}