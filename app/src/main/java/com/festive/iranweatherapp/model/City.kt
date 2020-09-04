package com.festive.iranweatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey val id: Int = -1,
    val name: String = "",
    val state: String = "",
    val lat: Double = Double.NaN,
    val lon: Double = Double.NaN
)