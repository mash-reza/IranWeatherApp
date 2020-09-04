package com.festive.iranweatherapp.repository.main.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.festive.iranweatherapp.model.City
import com.festive.iranweatherapp.model.Forecast

@Dao
interface MainDao {

    @Query("select * from City")
    suspend fun getCities(): List<City>

    @Query("select * from City where id = :id")
    suspend fun getCity(id: Int): City

    @Query("select * from Forecast where cityId=:id")
    fun getForecast(id: Int): LiveData<Forecast>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForecast(forecast: Forecast)
}