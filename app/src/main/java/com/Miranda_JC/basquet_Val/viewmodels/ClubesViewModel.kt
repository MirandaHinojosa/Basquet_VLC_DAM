package com.Miranda_JC.Basquet_Val.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Miranda_JC.Basquet_Val.data.Club
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClubesViewModel : ViewModel() {
    //conexion a la api
    private val apiService = RetrofitInstance.api

    private val _clubes = MutableStateFlow<List<Club>>(emptyList())
    val clubes: StateFlow<List<Club>> = _clubes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        cargarClubes()  //carga inicial de datos
    }

    // Carga lista de clubes desde API
    fun cargarClubes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = apiService.getClubes()
                if (response.isSuccessful) {
                    val dataResponse = response.body()
                    if (dataResponse?.success == true) {
                        _clubes.value = dataResponse.data ?: emptyList()
                    } else {
                        _error.value = "Error en la respuesta: ${dataResponse?.message ?: "Error desconocido"}"
                    }
                } else {
                    _error.value = "Error al cargar clubes: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    //actualiza query de busquda
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clubesFiltrados(): List<Club> {
        val query = _searchQuery.value.lowercase()
        return if (query.isBlank()) {
            _clubes.value
        } else {
            _clubes.value.filter { club ->
                club.nombre.lowercase().contains(query) ||
                        club.direccion?.lowercase()?.contains(query) == true ||
                        club.presidente?.lowercase()?.contains(query) == true
            }
        }
    }

    fun refresh() {
        cargarClubes()
    }
}