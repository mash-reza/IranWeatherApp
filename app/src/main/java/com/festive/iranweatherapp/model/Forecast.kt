package com.festive.iranweatherapp.model

import java.time.Instant

data class Forecast(
    val id: Int,
    val station: String,
    val date: Instant,
    val humidity: Double,
    val lat: Double,
    val lon: Double,
    val minTemp1: Double,
    val maxTemp1: Double,
    val minTemp2: Double,
    val maxTemp2: Double,
    val minTemp3: Double,
    val maxTemp3: Double,
    val day1: String,
    val day2: String,
    val day3: String,
    val windSpeed: Double,
    val windDirectionDegree: Int,
    val cloudCover: Int,
    val state: String
)