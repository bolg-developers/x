package com.example.bolg.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.adapter.holder.StandbyRecyclerViewHolder
import com.example.bolg.R
import com.example.bolg.data.ListData

// Clicl処理がある場合は引数にListnerをいれるかも
/** ----------------------------------------------------------------------
 * StandbyRecyclerAdapter
 * @param itemList SampleでListを追加している（多分消す）
 * ---------------------------------------------------------------------- */
class StandbyRecyclerAdapter(var itemList: List<ListData>) : RecyclerView.Adapter<StandbyRecyclerViewHolder>(){
    // 後で変更
    // var data =  List<ListData>
    //    var data =  itemList
    //        set(value) {
    //            field = value
    //            notifyDataSetChanged()
    //        }

    /** **********************************************************************
     * onCreateViewHolder
     * @param parent
     * @param viewType
     * @return StandbyRecyclerViewHolder(View)
     * ********************************************************************** */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandbyRecyclerViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return StandbyRecyclerViewHolder(rowView)
    }

    /** **********************************************************************
     * onBindViewHolder
     * @param holder
     * @param position
     * ********************************************************************** */
    override fun onBindViewHolder(holder: StandbyRecyclerViewHolder, position: Int) {
        holder.let {
            it.uname.text = itemList.get(position).uname
        }
    }

    /** **********************************************************************
     * onBindViewHolder
     * @param holder
     * @param position
     * @return size: Int
     * ********************************************************************** */
    override fun getItemCount(): Int {
        return itemList.size
    }
}