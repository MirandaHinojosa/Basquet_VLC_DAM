package com.Miranda_JC.Basquet_Val.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object ViewModelFactory {
    fun provideFactory(context: Context): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                AuthViewModel(context.applicationContext)
            }
        }
    }
}