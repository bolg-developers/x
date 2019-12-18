package com.example.bolg.main

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R


/** ----------------------------------------------------------------------
 * MainActivity
 * TitleFragmentの一定時間表示指示クラス
 * 上記が終了するとCreateJoinFragmentの表示指示
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val env : String? = System.getenv("Bolg")
        Log.d("path",env.toString())

        val application: Application = requireNotNull(this).application
        val viewModelFactory = MainViewModelFactory(application,supportFragmentManager)
        val mainViewModel: MainViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel::class.java)

        // title view
        mainViewModel.setTitle()

        mainViewModel.startTimer()

    }
}
