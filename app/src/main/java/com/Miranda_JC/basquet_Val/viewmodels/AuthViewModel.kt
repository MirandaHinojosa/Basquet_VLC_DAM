package com.Miranda_JC.Basquet_Val.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Miranda_JC.Basquet_Val.data.local.AppDatabase
import com.Miranda_JC.Basquet_Val.data.local.UsuarioLocal
import com.Miranda_JC.Basquet_Val.data.local.PerfilJugador
import com.Miranda_JC.Basquet_Val.data.RegisterRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//dataStore para persistencia de la ssesion
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

class AuthViewModel(private val context: Context) : ViewModel() {
    //acceso a la base de datos local (room)
    private val database = AppDatabase.getDatabase(context)
    private val usuarioDao = database.usuarioDao()
    private val perfilJugadorDao = database.perfilJugadorDao()

    //Keys para DataStore
    companion object {
        val USER_ID_KEY = intPreferencesKey("user_id")
        val USERNAME_KEY = stringPreferencesKey("username")
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }

    private val _currentUser = MutableStateFlow<UsuarioLocal?>(null)
    val currentUser: StateFlow<UsuarioLocal?> = _currentUser

    private val _currentPerfil = MutableStateFlow<PerfilJugador?>(null)
    val currentPerfil: StateFlow<PerfilJugador?> = _currentPerfil

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        //Carga sesión guardada al iniciar
        loadSavedSession()

    }

    private fun loadSavedSession() {
        viewModelScope.launch {
            context.dataStore.data.collect { preferences ->
                val isLoggedIn = preferences[IS_LOGGED_IN_KEY] ?: false
                val userId = preferences[USER_ID_KEY]

                if (isLoggedIn && userId != null) {
                    // Cargar usuario desde Room
                    usuarioDao.getUsuarioById(userId).collect { usuario ->
                        if (usuario != null) {
                            _currentUser.value = usuario
                            _isLoggedIn.value = true
                        } else {
                            // Sesión inválida, limpiar
                            clearSession()
                        }
                    }
                }
            }
        }
    }

    fun login(username: String, password: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                //Buscar usuario en Room
                val usuario = usuarioDao.login(username, password)

                if (usuario != null) {
                    //Guardar sesión
                    saveSession(usuario.id, usuario.username)
                    _currentUser.value = usuario
                    _isLoggedIn.value = true
                    callback(true, null)
                } else {
                    _error.value = "Usuario o contraseña incorrectos"
                    callback(false, "Usuario o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                callback(false, "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    //registramos el nuevo usuario
    fun register(registerRequest: RegisterRequest, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                //Verifica si ya existe
                val existe = usuarioDao.checkUsuarioExists(
                    registerRequest.username,
                    registerRequest.email
                )

                if (existe > 0) {
                    _error.value = "El usuario o email ya están registrados"
                    callback(false, "El usuario o email ya están registrados")
                    return@launch
                }

                //Crear usuario local
                val usuario = UsuarioLocal(
                    username = registerRequest.username,
                    email = registerRequest.email,
                    password = registerRequest.password
                )

                // Insertar usuario
                val userId = usuarioDao.insert(usuario).toInt()


                val usuarioConId = usuario.copy(id = userId)

                // Crear perfil básico
                val perfil = PerfilJugador(
                    usuarioId = userId,
                    nombreCompleto = registerRequest.username
                )
                perfilJugadorDao.insert(perfil)


                saveSession(userId, registerRequest.username)
                _currentUser.value = usuarioConId
                _currentPerfil.value = perfil
                _isLoggedIn.value = true

                callback(true, "Registro exitoso")

            } catch (e: Exception) {
                _error.value = "Error en el registro: ${e.message}"
                callback(false, "Error en el registro: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    // Actualiza perfil del jugador
    suspend fun updatePerfil(perfil: PerfilJugador): Boolean {
        return try {
            perfilJugadorDao.update(perfil)
            _currentPerfil.value = perfil
            true
        } catch (e: Exception) {
            _error.value = "Error al actualizar perfil: ${e.message}"
            false
        }
    }
    //cierra sesion
    fun logout() {
        viewModelScope.launch {
            clearSession()
            _currentUser.value = null
            _currentPerfil.value = null
            _isLoggedIn.value = false
            _error.value = null
        }
    }
    // Guarda sesión en DataStore
    private suspend fun saveSession(userId: Int, username: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = true
            preferences[USER_ID_KEY] = userId
            preferences[USERNAME_KEY] = username
        }
    }
    // Limpia sesión del DataStore
    private suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getPerfilFlow(usuarioId: Int) = perfilJugadorDao.getPerfilByUsuarioId(usuarioId)
}