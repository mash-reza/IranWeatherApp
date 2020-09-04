package com.festive.iranweatherapp.ui.main.choose

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.festive.iranweatherapp.R
import kotlinx.android.synthetic.main.choose_list_item.view.*

class ChooseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name:TextView = view.findViewById(R.id.chooseListItemNameTextView)
}