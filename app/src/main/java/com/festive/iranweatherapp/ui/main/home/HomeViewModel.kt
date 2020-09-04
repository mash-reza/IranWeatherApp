package com.festive.iranweatherapp.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.festive.iranweatherapp.model.City
import com.festive.iranweatherapp.model.Forecast
import com.festive.iranweatherapp.repository.main.MainRepository
import com.festive.iranweatherapp.repository.main.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(@Inject @JvmField var mainRepository: MainRepository) :
    ViewModel() {
    companion object {
        private val TAG = "HomeViewModel"
    }

    private val forecast: MediatorLiveData<Resource<Forecast?>> = MediatorLiveData()
    private val city: MutableLiveData<Resource<City?>> = MutableLiveData()


    init {
        Log.d(TAG, "${this.javaClass.name} is created")
    }

    fun getForecast(id: Int) {
        forecast.value = Resource.loading()
        CoroutineScope(Dispatchers.Main).launch {
            val source = mainRepository.getForecast(id)
            forecast.addSource(source) {
//                forecast.removeSource(source)
                forecast.postValue(it)
            }
        }
    }

    fun observeForecast(): LiveData<Resource<Forecast?>> {
        return forecast
    }

    fun setCity(id: Int) {
        city.value = Resource.loading()
        CoroutineScope(Dispatchers.Main).launch {
            city.postValue(mainRepository.getCity(id))
        }
    }

    fun observeCity() = city

}