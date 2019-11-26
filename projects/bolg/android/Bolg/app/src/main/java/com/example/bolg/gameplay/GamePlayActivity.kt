package com.example.bolg.gameplay

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R
import com.example.bolg.main.MainViewModel
import com.example.bolg.main.MainViewModelFactory

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * ---------------------------------------------------------------------- */
class GamePlayActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)


        val application: Application = requireNotNull(this).application
        val viewModelFactory = GamePlayViewModelFactory(application)
        val gamePlayViewModel:GamePlayViewModel = ViewModelProviders.of(this,viewModelFactory).get(GamePlayViewModel::class.java)

    }
}