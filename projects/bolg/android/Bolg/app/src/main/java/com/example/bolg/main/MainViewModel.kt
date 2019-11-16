package com.example.bolg.main

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import com.example.bolg.R
import kotlinx.coroutines.*

class MainViewModel (application: Application,fragmentManager: FragmentManager): AndroidViewModel(application){

    private var intentTimer = 0

    // Jobの定義
    private var viewModelJob = Job()

    // スコープの定義
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val mFragmentManager = fragmentManager


    fun setTitle(){

        Log.d("timer","onsetTitle")
        mFragmentManager.beginTransaction()
            .replace(
                R.id.carDetailContainer,
                TitleFtagment()
            )
            .commit()
    }

    fun startTimer(){
        Log.d("timer","onstartTimer")

        uiScope.launch {
            while (true) {
                delay(1000L)
                Log.d("timer", "onstartTimer")
                if (intentTimer++ == 4) {
                    //mFragmentManager.beginTransaction().remove(this).commit()
                    setCreateJoin()
                    break
                }
            }
        }
    }

    private fun setCreateJoin(){
        mFragmentManager.beginTransaction().replace(
            R.id.carDetailContainer,
            CreateJoinFragment()
        ).commit()
    }
}