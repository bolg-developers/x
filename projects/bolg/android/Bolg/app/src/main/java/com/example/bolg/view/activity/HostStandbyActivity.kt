package com.example.bolg.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.R

class HostStandbyActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_standby)

        val  rv = findViewById<RecyclerView>(R.id.standby_recycler_view)
        // LayoutManagerの設定
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        // Adapterの設定
        var sampleList = mutableListOf<String>()
        for (i in 0..10) {
            sampleList.add(i,"長谷川")
        }
        val adapter = StandbyRecyclerAdapter(sampleList)
        rv.adapter = adapter

        // 区切り線の表示
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

}