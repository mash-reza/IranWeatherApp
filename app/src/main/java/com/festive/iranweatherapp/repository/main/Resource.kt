package com.festive.iranweatherapp.repository.main

class Resource<T>(val status: AuthStatus, val data: T?, val message: String?) {

    enum class AuthStatus {
        SUCCESSFUL, ERROR, LOADING
    }

    companion object {
        fun <T> successful(data: T?): Resource<T?> {
            return Resource(AuthStatus.SUCCESSFUL, data, null)
        }

        fun <T> error(msg: String): Resource<T?> {
            return Resource(AuthStatus.ERROR, null, msg)
        }

        fun <T> loading(): Resource<T?> {
            return Resource(AuthStatus.LOADING, null, null)
        }
    }

}