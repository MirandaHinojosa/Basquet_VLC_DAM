package com.Miranda_JC.Basquet_Val.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Miranda_JC.Basquet_Val.data.Clasificacion
import com.Miranda_JC.Basquet_Val.data.Competicion
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompeticionesViewModel : ViewModel() {
    //conexion a la api
    private val apiService = RetrofitInstance.api

    //estados observables
    private val _competiciones = MutableStateFlow<List<Competicion>>(emptyList())
    val competiciones: StateFlow<List<Competicion>> = _competiciones


    private val _clasificacion = MutableStateFlow<List<Clasificacion>>(emptyList())
    val clasificacion: StateFlow<List<Clasificacion>> = _clasificacion

    private val _selectedCompeticion = MutableStateFlow<Competicion?>(null)
    val selectedCompeticion: StateFlow<Competicion?> = _selectedCompeticion

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Filtros
    private val _selectedTipo = MutableStateFlow<String?>(null)
    val selectedTipo: StateFlow<String?> = _selectedTipo

    private val _selectedCategoria = MutableStateFlow<String?>(null)
    val selectedCategoria: StateFlow<String?> = _selectedCategoria

    init {
        cargarCompeticiones()   //carga inicial
    }

    //carga todas las competicines
    fun cargarCompeticiones() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = apiService.getCompeticiones()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        // Extraemos el campo 'data' del response
                        _competiciones.value = body.data ?: emptyList()
                    } else {
                        _error.value = body?.message ?: "Error al cargar competiciones"
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

    fun cargarClasificacion(competicionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = apiService.getClasificacion(competicionId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {

                        _clasificacion.value = body.data ?: emptyList()
                    } else {
                        _error.value = body?.message ?: "Error al cargar clasificación"
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

    fun setSelectedCompeticion(competicion: Competicion?) {
        _selectedCompeticion.value = competicion
        if (competicion != null) {
            cargarClasificacion(competicion.id)
        }
    }
    //filtros
    fun setSelectedTipo(tipo: String?) {
        _selectedTipo.value = tipo
    }
    fun setSelectedCategoria(categoria: String?) {
        _selectedCategoria.value = categoria
    }

    //Filtra competiciones por tipo y categoría
    fun competicionesFiltradas(): List<Competicion> {
        return _competiciones.value.filter { competicion ->
            (selectedTipo.value == null || competicion.tipo == selectedTipo.value) &&
                    (selectedCategoria.value == null || competicion.categoria == selectedCategoria.value)
        }
    }
    //Obtiene lista de tipos únicos para filtros
    fun getTiposUnicos(): List<String> {
        return _competiciones.value.map { it.tipo }.distinct().sorted()
    }
    //Obtiene lista de categorías únicas para filtros
    fun getCategoriasUnicas(): List<String> {
        return _competiciones.value.map { it.categoria }.distinct().sorted()
    }

    fun refresh() {
        cargarCompeticiones()
    }
}