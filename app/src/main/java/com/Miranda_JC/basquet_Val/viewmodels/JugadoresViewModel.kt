package com.Miranda_JC.Basquet_Val.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Miranda_JC.Basquet_Val.data.Jugador
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class JugadoresViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api

    private val _jugadores = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadores: StateFlow<List<Jugador>> = _jugadores

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    //Filtros
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedEquipo = MutableStateFlow<String?>(null)
    val selectedEquipo: StateFlow<String?> = _selectedEquipo

    private val _selectedPosicion = MutableStateFlow<String?>(null)
    val selectedPosicion: StateFlow<String?> = _selectedPosicion

    init {
        cargarJugadores()   //carga inicial
    }
    //carga de jugadores
    fun cargarJugadores(equipo: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = apiService.getJugadores(equipo)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        //Extrae el jugador del campo 'data'
                        _jugadores.value = responseBody.data ?: emptyList()
                    } else {
                        _error.value = responseBody?.message ?: "Error en la respuesta"
                    }
                } else {
                    _error.value = "Error HTTP: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    //actualiza la busqueda pro texto
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedEquipo(equipo: String?) {
        _selectedEquipo.value = equipo
        cargarJugadores(equipo ?: "")
    }

    fun setSelectedPosicion(posicion: String?) {
        _selectedPosicion.value = posicion
    }
    //Aplica todos los filtros: búsqueda, equipo y posición
    fun jugadoresFiltrados(): List<Jugador> {
        val query = _searchQuery.value.lowercase()
        return _jugadores.value.filter { jugador ->
            val matchesSearch = query.isBlank() ||
                    jugador.nombre.lowercase().contains(query) ||
                    jugador.apellidos.lowercase().contains(query) ||
                    jugador.licenciaFederativa?.lowercase()?.contains(query) == true

            val matchesEquipo = _selectedEquipo.value == null ||
                    jugador.equipoActual?.lowercase()?.contains(_selectedEquipo.value?.lowercase() ?: "") == true

            val matchesPosicion = _selectedPosicion.value == null ||
                    jugador.posicion?.lowercase() == _selectedPosicion.value?.lowercase()

            matchesSearch && matchesEquipo && matchesPosicion
        }
    }

    fun getEquiposUnicos(): List<String> {
        return _jugadores.value
            .mapNotNull { it.equipoActual }
            .distinct()
            .sorted()
    }

    fun getPosicionesUnicas(): List<String> {
        return _jugadores.value
            .mapNotNull { it.posicion }
            .distinct()
            .sorted()
    }

    fun refresh() {
        cargarJugadores(_selectedEquipo.value ?: "")
    }
}