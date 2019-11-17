package com.example.bolg.standby.player

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlayerStandbyViewModelFactory (private val application: Application): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerStandbyViewModel::class.java)) {
            return PlayerStandbyViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}