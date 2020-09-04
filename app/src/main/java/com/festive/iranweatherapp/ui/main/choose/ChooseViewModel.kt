package com.festive.iranweatherapp.ui.main.choose

import androidx.lifecycle.*
import com.festive.iranweatherapp.model.City
import com.festive.iranweatherapp.repository.main.MainRepository
import com.festive.iranweatherapp.repository.main.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseViewModel @Inject constructor(@Inject @JvmField var mainRepository: MainRepository) :
    ViewModel() {

    private val cities: MutableLiveData<Resource<List<City>?>> = MutableLiveData()
//    private val city: MediatorLiveData<Resource<City?>> = MediatorLiveData()

    fun getCities() {
        cities.postValue(Resource.loading())
        CoroutineScope(Dispatchers.Main).launch {
            cities.postValue(mainRepository.getCities())

        }
    }

    fun observeCities(): LiveData<Resource<List<City>?>> {
        return cities
    }

//    fun getCity(id: Int): City {
//        city.value = Resource.loading()
//        CoroutineScope(Dispatchers.Main).launch{
//            val source = mainRepository.getCity(id)
//            city.addSource(source, Observer {
//                city.postValue(it)
//                city.removeSource(source)
//            })
//        }
//        return cities.value!!.data!![id]
//    }

//    fun observeCity(): LiveData<Resource<City?>> {
//        return city
//    }
}