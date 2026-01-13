package com.Miranda_JC.Basquet_Val.data

data class Equipo(
    val id: Int,
    val nombre: String,
    val clubId: Int?,
    val categoria: String?,
    val clubNombre: String? = null
)