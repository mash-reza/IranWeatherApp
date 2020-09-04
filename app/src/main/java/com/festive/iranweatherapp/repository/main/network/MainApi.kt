package com.festive.iranweatherapp.repository.main.network

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {
    @GET("data/2.5/weather?appid=c9edd74b10a4bccfeecf918863e1779c&lang=fa&units=metric")
    fun getForecast(@Query("id") id: Int): LiveData<ApiResponse<ForecastResponse>>
}