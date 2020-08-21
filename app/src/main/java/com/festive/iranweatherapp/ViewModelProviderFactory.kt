package com.festive.iranweatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory : ViewModelProvider.Factory {

    lateinit var creators: Map<Class<out ViewModel>, Provider<ViewModel>>
        @Inject set

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Provider<ViewModel>? = creators[modelClass]

        if (creator == null) {
            for (entry in creators.entries) {
                if (modelClass.isAssignableFrom(modelClass)) {
                    creator = entry.value
                    break
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("Unknown class passed to factory...")
        }

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}