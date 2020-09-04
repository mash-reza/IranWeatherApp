package com.festive.iranweatherapp.ui.main.choose

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festive.iranweatherapp.R
import com.festive.iranweatherapp.model.City

class ChooseRecyclerViewAdapter(val context: Context,private val cities: List<City>,private val onCityItemSelectListener: OnCityItemSelectListener) :
    RecyclerView.Adapter<ChooseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder =
        ChooseViewHolder(
            LayoutInflater.from(context).inflate(R.layout.choose_list_item, parent, false)
        )

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.name.text = cities[position].name
        holder.name.setOnClickListener {
            onCityItemSelectListener.onSelect(cities[position].id)
        }
    }
}