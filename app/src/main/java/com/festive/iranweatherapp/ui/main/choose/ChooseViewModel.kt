package com.festive.iranweatherapp.ui.main.choose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.festive.iranweatherapp.model.City
import com.festive.iranweatherapp.repository.main.MainRepository
import com.festive.iranweatherapp.repository.main.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import javax.inject.Inject

class ChooseViewModel @Inject constructor(@Inject @JvmField var mainRepository: MainRepository) :
    ViewModel() {

    private val cities: MutableLiveData<Resource<List<City>?>> = MutableLiveData()
    private val filteredCities: MutableLiveData<Resource<List<City>?>> = MutableLiveData()

    fun getCities() {
        filteredCities.postValue(Resource.loading())
        CoroutineScope(Dispatchers.Main).launch {
            filteredCities.postValue(async {
                cities.postValue(mainRepository.getCities())
                return@async cities


            }.await().value)
        }
    }

    fun filterCities(query: String) {

        if ((query.length > 1) && (cities.value is Resource.Success)) {
            filteredCities.value = Resource.successful(
                (
                        cities.value as Resource.Success).data?.stream()?.filter { city ->
                    return@filter city.name.startsWith(query)
                }?.collect(Collectors.toList())
            )
        } else if (query.isEmpty()) {
            filteredCities.value = cities.value
        }
    }

    fun observeCities(): LiveData<Resource<List<City>?>> {
        return filteredCities
    }


}