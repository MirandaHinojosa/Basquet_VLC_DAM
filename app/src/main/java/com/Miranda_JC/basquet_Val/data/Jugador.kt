package com.Miranda_JC.Basquet_Val.data

data class Jugador(
    val id: Int,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String?,
    val equipoActual: String?,
    val posicion: String?,
    val altura: Double?,
    val peso: Double?,
    val numeroCamiseta: Int?,
    val licenciaFederativa: String? = null
)