package com.festive.iranweatherapp.ui.main.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.festive.iranweatherapp.R
import com.festive.iranweatherapp.ViewModelProviderFactory
import com.festive.iranweatherapp.repository.main.Resource
import com.festive.iranweatherapp.repository.main.network.NoNetworkConnectivityException
import com.festive.iranweatherapp.ui.main.MainState
import com.festive.iranweatherapp.ui.main.MainViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.floor

class HomeFragment : DaggerFragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var mainViewModel: MainViewModel


    lateinit var homeViewModel:HomeViewModel

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(
            viewModelStore,
            viewModelProviderFactory
        ).get(MainViewModel::class.java)

        homeViewModel = ViewModelProvider(
           viewModelStore,
            viewModelProviderFactory
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCity()
        observeMainState()
        handleNavigateToChooseFragment()
        handleRetry()
    }

    private fun handleRetry() {
        homeFragmentErrorRetryButton.setOnClickListener {
            observeMainState()
        }
    }

    private fun handleNavigateToChooseFragment() {
        chooseFragmentCityNameTextView.setOnClickListener {
            mainViewModel.setMainState(MainState.Choose)
        }
    }

    private fun observeMainState() {
        mainViewModel.observeMainState().observe(viewLifecycleOwner, Observer {
            when (it) {
                MainState.Choose ->
                    navController.navigate(R.id.action_nav_home_to_chooseFragment)
                is MainState.Home -> observeCity()
            }
        })
    }

    private fun observeCity() {
        homeViewModel.observeCity().observe(viewLifecycleOwner, Observer {
//            requireActivity().toolbar.title = it.data?.name
            it?.let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        getForecast(resource.data!!.id)
                        observeForecast()
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {

                    }
                }
            }
        })
    }

    private fun setCity() {
        mainViewModel.observeMainState().value?.let {
            if (it is MainState.Home) {
                homeViewModel.setCity(it.id)
            }
        }
    }

    private fun getForecast(id: Int) {
        homeViewModel.getForecast(id)
    }

    private fun observeForecast() {
        homeViewModel.observeForecast().removeObservers(viewLifecycleOwner)
        homeViewModel.observeForecast().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "forecast loading...")
                    homeFragmentErrorLayout.visibility = View.GONE
                    homeFragmentInfoLayout.visibility = View.GONE
                    homeFragmentLoadingLayout.visibility = View.VISIBLE
                    homeFragmentLoadingLayout.bringToFront()
                }
                is Resource.Error -> {
                    Log.d(TAG, "forecast error: ${(it as Resource.Error).throwable.message}")
                    if (it.throwable is NoNetworkConnectivityException) {
                        homeFragmentErrorMessegeTextView.text =
                            "دستگاه به اینترنت متصل نیست! لطفا اتصال اینترنت را چک کرده و مجددا تلاش کنید!"
                        homeFragmentErrorIconImageView.setImageResource(R.drawable.internet_error)
                    } else {
                        homeFragmentErrorMessegeTextView.text = "خطایی رخ داده است!"
                        homeFragmentErrorIconImageView.setImageResource(R.drawable.error_icon)
                    }
                    homeFragmentInfoLayout.visibility = View.GONE
                    homeFragmentLoadingLayout.visibility = View.GONE
                    homeFragmentErrorLayout.visibility = View.VISIBLE
                    homeFragmentErrorLayout.bringToFront()
                }
                is Resource.Success -> {
                    homeFragmentErrorLayout.visibility = View.GONE
                    homeFragmentLoadingLayout.visibility = View.VISIBLE
                    homeFragmentLoadingLayout.bringToFront()
                    it.data?.let { data ->
                        chooseFragmentCityNameTextView.text =
                            (homeViewModel.observeCity().value as Resource.Success).data?.name
                                ?: data.name
                        glide.load(Uri.parse("file:///android_asset/${CodeToIconMapper.map[data.iconId]}"))
                            .into(nav_choose)
                        tempImageLoader(data.temp)
                        chooseFragmentDescriptionTextView.text = data.description
                        val dateFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")
                        dateFormat.timeZone = TimeZone.getTimeZone("GMT+4:30")
//                        dateFormat.timeZone = TimeZone.getTimeZone("Iran/Tehran")
                        chooseFragmentSunriseTextView.text =
                            dateFormat.format(Date(data.sunrise * 1000))
                        chooseFragmentSunsetTextview.text =
                            dateFormat.format(Date(data.sunset * 1000))
                        chooseFragmentTempTextView.text = "${data.temp.toInt()}c"
                        chooseFragmentVisibilityTextView.text = "${data.visibility / 1000} کیلومتر"
                        chooseFragmentMaxTempTextView.text = "${data.tempMax.toInt()}c"
                        chooseFragmentMinTempTextView.text = "${data.tempMin.toInt()}c"
                        chooseFragmentPressureTextView.text = "${data.pressure} اتمسفر"
                        chooseFragmentHumidityTextView.text = "${data.humidity}%"
                        chooseFragmentWindSpeedTextView.text = "${data.windSpeed} kmh"
                        chooseFragmentWindDegreeTextView.text =
                            convertWindDegreeToCompassDirection(data.windDegree)
                        homeFragmentLoadingLayout.visibility = View.GONE
                        homeFragmentInfoLayout.visibility = View.VISIBLE
                        homeFragmentInfoLayout.bringToFront()
                    }
                }
            }
        })
    }


    private fun convertWindDegreeToCompassDirection(degree: Int): String {
        Log.d(TAG, "convertWindDegreeToCompassDirection: $degree")
        val directions =
            arrayOf("شمال", "شمال شرق", "شرق", "جنوب شرق", "جنوب", "جنوب غرب", "غرب", "شمال غرب")
        floor(((degree / 45) + .5)).toInt().let {
            Log.d(TAG, "convertWindDegreeToCompassDirection: index $it")
            if (it == 8)
                return directions[0]
            return directions[it]
        }
    }

    private fun tempImageLoader(temp: Double) {
        when {
            temp.toInt() < 0 -> {
                glide.load(R.drawable.temp_01)
                    .into(chooseFragmentTempImageView)
            }
            temp.toInt() in 0..9 -> {
                glide.load(R.drawable.temp_02)
                    .into(chooseFragmentTempImageView)
            }
            temp.toInt() in 10..19 -> {
                glide.load(R.drawable.temp_03)
                    .into(chooseFragmentTempImageView)
            }
            temp.toInt() in 20..29 -> {
                glide.load(R.drawable.temp_04)
                    .into(chooseFragmentTempImageView)
            }
            temp.toInt() in 30..100 -> {
                glide.load(R.drawable.temp_05)
                    .into(chooseFragmentTempImageView)
            }
        }
    }
}