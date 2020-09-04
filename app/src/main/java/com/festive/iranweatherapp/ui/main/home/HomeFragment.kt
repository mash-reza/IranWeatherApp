package com.festive.iranweatherapp.ui.main.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.festive.iranweatherapp.R
import com.festive.iranweatherapp.repository.main.Resource
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
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        getForecast(18018)
//        observeForecast()
//        getCities()
//        observeCities()
//        getChosenCity(119505)
//        observeChosenCity()

        setCity()
        observeMainState()
        navigateToChooseFragment()
    }

    private fun navigateToChooseFragment() {
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
            it.data?.let { city ->
                getForecast(city.id)
                observeForecast()
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
            when (it.status) {
                Resource.AuthStatus.LOADING -> {
                    Log.d(TAG, "forecast loading...")
                    chooseFragmentErrorLayout.visibility = View.GONE
                    chooseFragmentInfoLayout.visibility = View.GONE
                    chooseFragmentLoadingLayout.visibility = View.VISIBLE
                    chooseFragmentLoadingLayout.bringToFront()
                }
                Resource.AuthStatus.ERROR -> {
                    Log.d(TAG, "forecast error: ${it.message}")
                    chooseFragmentInfoLayout.visibility = View.GONE
                    chooseFragmentLoadingLayout.visibility = View.GONE
                    chooseFragmentErrorLayout.visibility = View.VISIBLE
                    chooseFragmentErrorLayout.bringToFront()
                }
                Resource.AuthStatus.SUCCESSFUL -> {
                    chooseFragmentErrorLayout.visibility = View.GONE
                    chooseFragmentLoadingLayout.visibility = View.VISIBLE
                    chooseFragmentLoadingLayout.bringToFront()
                    it.data?.let { data ->
                        chooseFragmentCityNameTextView.text = homeViewModel.observeCity().value?.data?.name?:data.name
                        glide.load(Uri.parse("file:///android_asset/${CodeToIconMapper.map[data.iconId]}"))
                            .into(chooseFragmentWeatherIMageView)
                        tempImageLoader(data.temp)
                        chooseFragmentDescriptionTextView.text = data.description
                        val dateFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")
                        dateFormat.timeZone = TimeZone.getTimeZone("GMT+4:30")
//                        dateFormat.timeZone = TimeZone.getTimeZone("Iran/Tehran")
                        chooseFragmentSunriseTextView.text =
                            dateFormat.format(Date(data.sunrise*1000))
                        chooseFragmentSunsetTextview.text =
                            dateFormat.format(Date(data.sunset*1000))
                        chooseFragmentTempTextView.text = "${data.temp.toInt()}c"
                        chooseFragmentVisibilityTextView.text = "${data.visibility / 1000} کیلومتر"
                        chooseFragmentMaxTempTextView.text = "${data.tempMax.toInt()}c"
                        chooseFragmentMinTempTextView.text = "${data.tempMin.toInt()}c"
                        chooseFragmentPressureTextView.text = "${data.pressure} اتمسفر"
                        chooseFragmentHumidityTextView.text = "${data.humidity}%"
                        chooseFragmentWindSpeedTextView.text = "${data.windSpeed} kmh"
                        chooseFragmentWindDegreeTextView.text =
                            convertWindDegreeToCompassDirection(data.windDegree)
                        chooseFragmentLoadingLayout.visibility = View.GONE
                        chooseFragmentInfoLayout.visibility = View.VISIBLE
                        chooseFragmentInfoLayout.bringToFront()
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