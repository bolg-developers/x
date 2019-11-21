package com.example.bolg.standby.player

import android.app.Application
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.R
import com.example.bolg.data.ListData

/** ----------------------------------------------------------------------
 * クラス名 PlayerStandbyActivity
 * ・概要1
 * ・概要2
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class PlayerStandbyActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_standby)

        // widget init
        val progress  : ProgressBar =  findViewById(R.id.progress)
        val joinuser  : RecyclerView = findViewById(R.id.standby_recycler_view)
        val ready     : Button = findViewById(R.id.player_ready_btn)
        val rule      : TextView = findViewById(R.id.rule)

        // viewModelインスタンス
        val application: Application = requireNotNull(this).application
        val viewModelFactoryplayer: PlayerStandbyViewModelFactory = PlayerStandbyViewModelFactory(application)
        val playerstandbyViewModel = ViewModelProviders.of(this,viewModelFactoryplayer).get(PlayerStandbyViewModel::class.java)

        // LayoutManagerの設定
        val layoutManager = LinearLayoutManager(this)
        joinuser.layoutManager = layoutManager
        // Adapterの設定
        var sampleList = mutableListOf<ListData>()
        for (i in 0..10) {
            sampleList.add(i, ListData("hasegawa${i}"))
        }
        val adapter = StandbyRecyclerAdapter(sampleList)
        joinuser.adapter = adapter
        // 区切り線の表示
        joinuser.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // Observe類
        playerstandbyViewModel.gameRule.observe(this, Observer { mrule->
            rule.text = mrule
        })

        playerstandbyViewModel.itemState.observe(this, Observer { item->
            if(item){
                // 青色に変化（ONっぽい表示）
            }else{

            }
        })

        playerstandbyViewModel.kakinBulletState.observe(this, Observer { kakinbullet->
            if(kakinbullet){
                // 青色に変化（ONっぽい表示）
            }else{

            }
        })


        // Click処理
        ready.setOnClickListener { progress.visibility = ProgressBar.VISIBLE }


    }
}