package com.festive.iranweatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Forecast(
    @PrimaryKey
    val cityId:Int=-1,
    val iconId: Int = -1,
    val description: String = "",
    val icon: String = "",
    val temp: Double = Double.NaN,
    @ColumnInfo(name = "temp_min")
    val tempMin: Double = Double.NaN,
    @ColumnInfo(name = "temp_max")
    val tempMax: Double = Double.NaN,
    val humidity: Int = -1,
    val pressure: Int = -1,
    val visibility: Int = -1,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double = Double.NaN,
    @ColumnInfo(name = "wind_degree")
    val windDegree: Int = -1,
    val sunrise: Long = -1,
    val sunset: Long = -1,
    val date: Long = -1,
    val name: String = "",
    @ColumnInfo(name = "server_code")
    val serverCode: Int = -1
)