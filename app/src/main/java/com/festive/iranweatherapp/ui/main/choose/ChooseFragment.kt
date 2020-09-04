package com.festive.iranweatherapp.ui.main.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.festive.iranweatherapp.R
import com.festive.iranweatherapp.ui.main.MainState
import com.festive.iranweatherapp.ui.main.MainViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.choose_fragment.*
import javax.inject.Inject

class ChooseFragment : DaggerFragment() {

    companion object {
        private const val TAG = "ChooseFragment"
    }

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var chooseViewModel: ChooseViewModel

    @Inject
    lateinit var navController: NavController

    lateinit var adapter: ChooseRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCities()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeCities()
        observeMainState()
    }

    private fun initRecyclerView() {
        chooseFragmentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun getCities() = chooseViewModel.getCities()

    private fun observeCities() {
        chooseViewModel.observeCities().observe(viewLifecycleOwner, Observer {
            it.data?.let { data ->
                adapter =
                    ChooseRecyclerViewAdapter(requireContext(), data, OnCityItemSelectListener {
                        setCity(it)
                    })
                chooseFragmentRecyclerView.adapter = adapter
            }
        })
    }

    private fun setCity(id: Int) {
        mainViewModel.setMainState(MainState.Home(id))
    }

    private fun observeMainState() {
        mainViewModel.observeMainState().observe(requireActivity(), Observer {
            if (it is MainState.Home) {
                navController.navigate(R.id.action_chooseFragment_to_nav_home)
            }
        })
    }

//    private fun getChosenCity() {
//        mainViewModel
//        chooseViewModel.getCity(id)
//    }

//    private fun observeChosenCity() {
//        chooseViewModel.observeCity().observe(viewLifecycleOwner, Observer {
//            it.data?.let { data ->
//                textView2.text =
//                    "id ${data.id} name ${data.name} state ${data.state} lat ${data.lat} lon ${data.lon}"
//            }
//        })
//    }
}