package com.festive.iranweatherapp.ui.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(@Inject @JvmField var context: Application) : ViewModel() {

    private val mainState: MutableLiveData<MainState> = MutableLiveData()
//    private val cityId:MutableLiveData<Int> = MutableLiveData()

    companion object {
        private const val TAG = "MainViewModel"
        private const val CITY_PREF_NAME = "cityprefname"
        private const val CHOSEN_CITY_PREF_KEY = "chosencityprefkey"
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences(CITY_PREF_NAME, Context.MODE_PRIVATE)

    init {
        val cityId = pref.getInt(CHOSEN_CITY_PREF_KEY, -1)
        if (cityId == -1)
            mainState.value = MainState.Choose
        else mainState.value = MainState.Home(cityId)
        Log.d(TAG, "setMainState called and city id  is ${pref.getInt(CHOSEN_CITY_PREF_KEY, -1)} ")
    }


    fun setMainState(state: MainState) {
        pref.edit {
            if (state == MainState.Choose)
                putInt(CHOSEN_CITY_PREF_KEY, -1)
            else putInt(CHOSEN_CITY_PREF_KEY, (state as MainState.Home).id)
        }
        mainState.value = state
    }

    fun observeMainState(): MutableLiveData<MainState> {
        return mainState
    }
}