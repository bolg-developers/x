package com.example.bolg.standby.host

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bolg.standby.player.PlayerStandbyViewModel

class HostStandbyViewModelFactory (private val application: Application): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HostStandbyViewModel::class.java)) {
            return HostStandbyViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}