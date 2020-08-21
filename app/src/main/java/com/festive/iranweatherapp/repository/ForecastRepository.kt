package com.festive.iranweatherapp.repository

import retrofit2.Retrofit
import javax.inject.Inject

class ForecastRepository @Inject constructor() {

    lateinit var retrofit: Retrofit
        @Inject set

    public fun getForecast() {}
}