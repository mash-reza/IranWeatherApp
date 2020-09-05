package com.festive.iranweatherapp.repository.main

sealed class Resource<T> {

    companion object {
        fun <T> successful(data: T?): Resource<T?> = Success(data)

        fun <T> error(error: Throwable): Resource<T?> = Error(error)

        fun <T> loading(): Resource<T?> = Loading()
    }

    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val throwable: Throwable) : Resource<T>()
    class Loading<T> : Resource<T>()
}