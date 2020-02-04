package com.example.bolg.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import com.example.bolg.R
import com.example.bolg.main.createandJoin.CreateJoinFragment
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
class MainViewModel (
    application: Application,
    fragmentManager: FragmentManager
): AndroidViewModel(application){

    /** Coroutine定義 **/

    // Fragmentの操作
    private val mFragmentManager: FragmentManager = fragmentManager

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

    /** **********************************************************************
     * initStaminaPreferences
     * スタミナ情報の初期化(getSharedPreferences)
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun initStaminaPreferences(app:Application){
        /** stamina info sharedPreferences **/
        val editor: SharedPreferences.Editor? =
            app.getSharedPreferences(
                "RoomDataSave", Context.MODE_PRIVATE)
                .edit()

        editor?.putBoolean("staminaFirst", true)
        editor?.putBoolean("staminaSecond", true)
        editor?.putBoolean("staminaThird", true)
        editor?.putLong("nowTimer", 0L)
        editor?.putBoolean("loop_state",false)
        editor?.apply()
    }


    /** **********************************************************************
     * setTitle
     * タイトルの表示
     * @author 長谷川　勇太
     * ********************************************************************** */
//    fun setTitle(){
//        Log.d("main/title","onsetTitle")
//
//        mFragmentManager.beginTransaction()
//            .replace(
//                R.id.mainContainer,
//                TitleFragment()
//            )
//            .commit()
//    }

    /** **********************************************************************
     * startTimer
     * タイトル表示のカウントアップ
     * @author 長谷川　勇太
     * ********************************************************************** */
//    fun startTimer(){
//        Log.d("main/title","onstartTimer")
//
//        uiScope.launch {
//            while (true) {
//                delay(500L)
//                Log.d("main/title", "onstartTimer\nintentTimer:${intentTimer}")
//                if (intentTimer++ == 3) {
//                    Log.d("main/title", "onstartTimer/intentTimer == 3")
//                    setCreateJoin()
//                    break
//                }
//            }
//        }
//    }


}