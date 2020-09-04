package com.festive.iranweatherapp.repository.main.network

import com.google.gson.annotations.SerializedName

data class ForecastResponse constructor(
    @SerializedName("weather")
    val weathers: Array<Weather> = arrayOf(),
    val main: Main = Main(),
    val visibility: Int = -1,
    val wind: Wind = Wind(),
    @SerializedName("sys")
    val system: System = System(),
    @SerializedName("dt")
    val date: Long = -1,
    val name: String = "",
    @SerializedName("cod")
    val statusCode: Int = -1,
    @SerializedName("id")
    val cityId: Int = -1,
    val error: Boolean = false,
    val errorType: ErrorType= ErrorType.OTHER
) {
    data class Main(
        val temp: Double = Double.NaN,
        val temp_min: Double = Double.NaN,
        val temp_max: Double = Double.NaN,
        val humidity: Int = -1,
        val pressure: Int = -1
    )

    data class Weather(
        @SerializedName("id")
        val iconId: Int = -1,
        val description: String = "",
        val icon: String = ""
    )

    data class Wind(
        val speed: Double = Double.NaN,
        val deg: Int = -1
    )

    data class System(
        val sunrise: Long = -1,
        val sunset: Long = -1
    )

    enum class ErrorType {
        HTTP, NETWORK, OTHER
    }
}