package com.example.bolg.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.adapter.Holder.StandbyRecyclerViewHolder
import com.example.bolg.R

// Clicl処理がある場合は引数にListnerをいれるかも
class StandbyRecyclerAdapter(var itemList: List<String>) : RecyclerView.Adapter<StandbyRecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandbyRecyclerViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return StandbyRecyclerViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: StandbyRecyclerViewHolder, position: Int) {
        holder.let {
            it.uname.text = itemList.get(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}