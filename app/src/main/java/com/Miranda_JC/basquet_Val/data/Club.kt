package com.Miranda_JC.Basquet_Val.data

data class Club(
    val id: Int,
    val nombre: String,
    val direccion: String?,
    val telefono: String?,
    val email: String?,
    val presidente: String?,
    val logoUrl: String?
)