package com.example.bolg.adapter.Holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.R

class StandbyRecyclerViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val uname: TextView = view.findViewById(R.id.user_name)
}