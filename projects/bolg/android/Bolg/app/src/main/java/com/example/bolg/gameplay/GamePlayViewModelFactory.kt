package com.example.bolg.gameplay

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bolg.main.MainViewModel

/** ----------------------------------------------------------------------
 * GamePlayViewModelFactory
 * @param application : ViewModelの引数に使用
 * ViewModelのインスタンスの取得
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class GamePlayViewModelFactory (private val application: Application): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GamePlayViewModel::class.java)) {
            return GamePlayViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}