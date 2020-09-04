package com.festive.iranweatherapp.repository.main.network

import retrofit2.Response
import java.lang.RuntimeException


sealed class ApiResponse<E> {

    companion object {
        fun <E> create(throwable: Throwable): Error<E> =
            ApiResponse.Error(throwable)

        fun <E> create(response: Response<E>): ApiResponse<E> {
            return if (response.isSuccessful)
            {
                val body = response.body() as E
                if (body == null) {
                    Empty()
                } else {
                    Success(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                Error(RuntimeException(errorMsg))
            }
        }
    }

    class Empty<E> : ApiResponse<E>()

    data class Success<E>(val body: E) : ApiResponse<E>()

    data class Error<E>(val error: Throwable) : ApiResponse<E>()
}