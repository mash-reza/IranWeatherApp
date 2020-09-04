package com.festive.iranweatherapp.repository.main.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.festive.iranweatherapp.model.City
import com.festive.iranweatherapp.model.Forecast

@Database(entities = [City::class,Forecast::class],version = 1,exportSchema = false)
abstract class MainDatabase:RoomDatabase() {
    abstract fun dao():MainDao
}