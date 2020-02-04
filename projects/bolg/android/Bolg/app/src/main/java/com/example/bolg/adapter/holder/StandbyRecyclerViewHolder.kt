package com.example.bolg.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.R

/** ----------------------------------------------------------------------
 * StandbyRecyclerViewHolder
 * @param view : Listに表示するviewの設定
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class StandbyRecyclerViewHolder (
    view: View
) : RecyclerView.ViewHolder(view) {
    val uname: TextView = view.findViewById(R.id.user_name)
}