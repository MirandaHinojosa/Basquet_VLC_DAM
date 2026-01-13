package com.Miranda_JC.Basquet_Val.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

//Factory para crear instancias de AuthViewModel con dependencias
class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //Verifica si la clase solicitada es AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(context) as T   //crea la instancia con contexto
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}