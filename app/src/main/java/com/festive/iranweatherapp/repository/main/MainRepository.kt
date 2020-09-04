package com.festive.iranweatherapp.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.festive.iranweatherapp.model.City
import com.festive.iranweatherapp.model.Forecast
import com.festive.iranweatherapp.repository.main.database.MainDao
import com.festive.iranweatherapp.repository.main.network.ForecastResponse
import com.festive.iranweatherapp.repository.main.network.MainApi
import com.festive.iranweatherapp.repository.main.network.ApiResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor() {

    companion object {
        private const val TAG = "MainRepository"
    }

    @Inject
    lateinit var mainApi: MainApi

    @Inject
    lateinit var mainDao: MainDao


    suspend fun getForecast(id: Int): LiveData<Resource<Forecast?>> {
        return object : NetworkBoundResource<Forecast, ForecastResponse>() {
            override suspend fun saveCallResult(item: ForecastResponse) {
                mainDao.saveForecast(item.mapToForecast())
            }

            override fun shouldFetch(data: Forecast?): Boolean {
                return (data == null) || (data.iconId == -1) || (isForecastExpired(data))
            }

            override suspend fun loadFromDb(): LiveData<Forecast> {
                return mainDao.getForecast(id)
            }

            override fun createCall(): LiveData<ApiResponse<ForecastResponse>> =
                    mainApi.getForecast(id)
        }.asLiveData()
    }

    private fun isForecastExpired(forecast: Forecast) =
        ((Date().time / 1000) - forecast.date) > 3600


    suspend fun getCities(): Resource<List<City>?> {
        return Resource.successful(mainDao.getCities())
    }

    suspend fun getCity(id: Int): Resource<City?> = Resource.successful(mainDao.getCity(id))

    fun ForecastResponse.mapToForecast(): Forecast =
        Forecast(
            cityId,
            weathers[0].iconId,
            weathers[0].description,
            weathers[0].icon,
            main.temp,
            main.temp_min,
            main.temp_max,
            main.humidity,
            main.pressure,
            visibility,
            wind.speed,
            wind.deg,
            system.sunrise,
            system.sunset,
            date,
            name,
            statusCode
        )

}
