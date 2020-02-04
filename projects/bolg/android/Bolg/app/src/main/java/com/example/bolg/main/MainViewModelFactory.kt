package com.example.bolg.main

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/** ----------------------------------------------------------------------
 * MainViewModelFactory
 * @param application : ViewModelの引数に使用
 * @param fragmentManager : Fragmentの操作
 * ViewModelのインスタンスの取得
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class MainViewModelFactory (
    private val application: Application,
    private val fragmentManager: FragmentManager
): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application,fragmentManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}