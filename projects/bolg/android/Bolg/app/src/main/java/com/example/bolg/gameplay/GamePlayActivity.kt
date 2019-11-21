package com.example.bolg.gameplay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bolg.R

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * ---------------------------------------------------------------------- */
class GamePlayActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
    }
}