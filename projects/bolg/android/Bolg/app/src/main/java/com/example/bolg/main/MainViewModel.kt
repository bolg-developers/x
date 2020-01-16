package com.example.bolg.main

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import com.example.bolg.R
import com.example.bolg.main.createandJoin.CreateJoinFragment
import com.example.bolg.main.title.TitleFtagment
import kotlinx.coroutines.*

/** ----------------------------------------------------------------------
 * MainViewModel
 * @param application : AndroidViewModelの引数に使用
 * @param fragmentManager : Fragmentの操作
 * MainActivityの処理部分担う
 * ・Titleの表示
 * ・参加/部屋生成画面へ遷移
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class MainViewModel (application: Application,fragmentManager: FragmentManager): AndroidViewModel(application){

    // titleViewTimer
    private var intentTimer = 0

    /** Coroutine定義 **/
    // Job Set
    private var viewModelJob = Job()
    // Scope Set
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Fragmentの操作
    private val mFragmentManager: FragmentManager = fragmentManager

    /** **********************************************************************
     * setTitle
     * タイトルの表示
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun setTitle(){
        Log.d("main/title","onsetTitle")

        mFragmentManager.beginTransaction()
            .replace(
                R.id.mainContainer,
                TitleFtagment()
            )
            .commit()
    }

    /** **********************************************************************
     * startTimer
     * タイトル表示のカウントアップ
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun startTimer(){
        Log.d("main/title","onstartTimer")

        uiScope.launch {
            while (true) {
                delay(500L) // 間隔は適当
                Log.d("main/title", "onstartTimer\nintentTimer:${intentTimer}")
                if (intentTimer++ == 3) {
                    Log.d("main/title", "onstartTimer/intentTimer == 3")
                    setCreateJoin()
                    break
                }
            }
        }
    }

    /** **********************************************************************
     * setCreateJoin
     * 参加/部屋生成画面へ遷移
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun setCreateJoin(){
        Log.d("main/title","onsetCreateJoin")

        mFragmentManager.beginTransaction().replace(
            R.id.mainContainer,
            CreateJoinFragment()
        ).commit()
    }
}