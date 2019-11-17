package com.example.bolg.main

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val application: Application = requireNotNull(this).application

        val viewModelFactory: MainViewModelFactory = MainViewModelFactory(application,supportFragmentManager)

        val mainViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel::class.java)

        mainViewModel.setTitle()

        mainViewModel.startTimer()

    }
}
