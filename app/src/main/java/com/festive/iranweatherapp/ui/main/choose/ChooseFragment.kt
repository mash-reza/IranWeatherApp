package com.festive.iranweatherapp.ui.main.choose

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.festive.iranweatherapp.R
import com.festive.iranweatherapp.ViewModelProviderFactory
import com.festive.iranweatherapp.repository.main.Resource
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
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var mainViewModel: MainViewModel


    lateinit var chooseViewModel: ChooseViewModel

    @Inject
    lateinit var navController: NavController

    lateinit var adapter: ChooseRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(
            viewModelStore,
            viewModelProviderFactory
        ).get(MainViewModel::class.java)
        chooseViewModel = ViewModelProvider(
            viewModelStore,
            viewModelProviderFactory
        ).get(ChooseViewModel::class.java)
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
        handleSearch()
    }

    private fun initRecyclerView() {
        chooseFragmentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
    }

    private fun handleSearch() {
        chooseFragmentSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                chooseViewModel.filterCities(s.toString())
            }

        })
    }

    private fun getCities() = chooseViewModel.getCities()

    private fun observeCities() {
        chooseViewModel.observeCities().observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    adapter =
                        ChooseRecyclerViewAdapter(
                            requireContext(),
                            resource.data!!,
                            OnCityItemSelectListener {
                                setCity(it)
                            })
                    chooseFragmentRecyclerView.adapter = adapter
                    chooseFragmentLoadingLayout.visibility = View.GONE
                    chooseFragmentSuccessLayout.visibility = View.VISIBLE
                    chooseFragmentSuccessLayout.bringToFront()
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    chooseFragmentSuccessLayout.visibility = View.GONE
                    chooseFragmentLoadingLayout.visibility = View.VISIBLE
                    chooseFragmentLoadingLayout.bringToFront()
                }
            }
        })
    }

    private fun setCity(id: Int) {
        mainViewModel.setMainState(MainState.Home(id))
    }

    private fun observeMainState() {
        mainViewModel.observeMainState().observe(viewLifecycleOwner, Observer {
            if (it is MainState.Home) {
                navController.navigate(R.id.action_nav_choose_to_nav_home)
            }
        })
    }
}