package com.festive.iranweatherapp.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.festive.iranweatherapp.repository.main.network.ApiResponse
import kotlinx.coroutines.*

abstract class NetworkBoundResource<ResultType, RequestType> {
    companion object {
        private const val TAG = "NetworkBoundResource"
    }

    val result = MediatorLiveData<Resource<ResultType?>>()

    init {
        Log.d(
            TAG,
            "has active observers: ${result.hasActiveObservers()} has observers: ${result.hasObservers()}"
        )
        setValue(Resource.loading())
        CoroutineScope(Dispatchers.IO).launch {
            val dbSource = loadFromDb()
            Log.d(TAG, "dbsource1 : $dbSource")
            withContext(Dispatchers.Main) {
                result.addSource(dbSource) { data ->
                    result.removeSource(dbSource)
                    if (shouldFetch(data)) {
                        fetchFromNetwork(dbSource)
                    } else {
                        result.addSource(dbSource) { newData ->
                            setValue(Resource.successful(newData))
                        }
                    }
                }
            }
        }
    }

    private fun setValue(newValue: Resource<ResultType?>) {
        if (result.value != newValue)
            result.value = newValue
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) {
            setValue(Resource.loading())
        }
        result.addSource(apiResponse) {response->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiResponse.Empty -> {
                    result.addSource(dbSource) {
                        setValue(Resource.successful(it))
                    }
                }
                is ApiResponse.Error -> {
                    onFetchFailed()
                    result.addSource(dbSource) {
                        setValue(Resource.error(response.error))
                    }
                }
                is ApiResponse.Success -> {
                    result.addSource(apiResponse) {
//                        setValue(Resource.successful(processResponse(it as ApiResponse.Success)))
                        CoroutineScope(Dispatchers.Main).launch {
                            async(Dispatchers.IO) {
                                saveCallResult(processResponse(it as ApiResponse.Success))
                            }.await().let {
                                withContext(Dispatchers.Main) {
                                    result.addSource(loadFromDb()) {
                                        setValue(Resource.successful(it))
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType?>>

    protected open fun processResponse(response: ApiResponse.Success<RequestType>) =
        response.body

    protected abstract suspend fun saveCallResult(item: RequestType)

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun loadFromDb(): LiveData<ResultType>

    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

}