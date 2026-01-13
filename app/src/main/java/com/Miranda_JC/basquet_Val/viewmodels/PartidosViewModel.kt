package com.Miranda_JC.Basquet_Val.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Miranda_JC.Basquet_Val.data.Partido
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PartidosViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api


    private val _partidosEnDirecto = MutableStateFlow<List<Partido>>(emptyList())
    val partidosEnDirecto: StateFlow<List<Partido>> = _partidosEnDirecto.asStateFlow()

    private val _partidosProgramados = MutableStateFlow<List<Partido>>(emptyList())
    val partidosProgramados: StateFlow<List<Partido>> = _partidosProgramados.asStateFlow()

    private val _partidosFinalizados = MutableStateFlow<List<Partido>>(emptyList())
    val partidosFinalizados: StateFlow<List<Partido>> = _partidosFinalizados.asStateFlow() // Cambiado a StateFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    // Carga partidos en directo (estado: EN_CURSO)
    fun cargarPartidosEnDirecto() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getPartidosEnDirecto()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        _partidosEnDirecto.value = responseBody.data ?: emptyList()
                    } else {
                        _error.value = responseBody?.message ?: "Error en la respuesta"
                    }
                } else {
                    _error.value = "Error HTTP: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar partidos en directo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    // Carga partidos programads (estado: PROGRAMADOS)
    fun cargarPartidosProgramados() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getPartidosProgramados()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        _partidosProgramados.value = responseBody.data ?: emptyList()
                    } else {
                        _error.value = responseBody?.message ?: "Error en la respuesta"
                    }
                } else {
                    _error.value = "Error HTTP: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar partidos programados: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    // Carga partidos finalizados (estado: FINALIZADO)
    fun cargarPartidosFinalizados() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = apiService.getPartidosFinalizados()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        _partidosFinalizados.value = responseBody.data ?: emptyList()
                    } else {
                        _error.value = responseBody?.message ?: "Error en la respuesta"
                    }
                } else {
                    _error.value = "Error HTTP: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar partidos finalizados: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }
    // Actualiza query de búsqueda
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun partidosFiltrados(partidos: List<Partido>): List<Partido> {
        val query = _searchQuery.value.lowercase()
        if (query.isBlank()) return partidos

        return partidos.filter { partido ->
            partido.equipoLocalNombre?.lowercase()?.contains(query) == true ||
                    partido.equipoVisitanteNombre?.lowercase()?.contains(query) == true ||
                    partido.competicionNombre?.lowercase()?.contains(query) == true ||
                    partido.pabellon?.lowercase()?.contains(query) == true
        }
    }

    fun refresh() {
        when (_selectedTab.value) {
            0 -> cargarPartidosProgramados()
            1 -> cargarPartidosEnDirecto()
            2 -> cargarPartidosFinalizados()
        }
    }
}