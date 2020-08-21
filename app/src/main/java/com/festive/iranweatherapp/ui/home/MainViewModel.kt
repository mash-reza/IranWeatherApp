package com.festive.iranweatherapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.festive.iranweatherapp.repository.ForecastRepository
import javax.inject.Inject

class MainViewModel() : ViewModel() {
    companion object {
        private val TAG = "MainViewModel"
    }

    lateinit var forecastRepository: ForecastRepository
        @Inject set

    init {
        Log.d(TAG, "${this.javaClass.name} is created")
    }


}