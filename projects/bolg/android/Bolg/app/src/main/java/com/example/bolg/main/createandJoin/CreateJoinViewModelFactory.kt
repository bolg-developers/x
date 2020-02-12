package com.example.bolg.main.createandJoin

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/** ----------------------------------------------------------------------
 * CreateJoinViewModelFactory
 * @param application : ViewModelの引数に使用
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class CreateJoinViewModelFactory (
    private val application: Application
): ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateJoinViewModel::class.java)) {
            return CreateJoinViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}