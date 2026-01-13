package com.Miranda_JC.Basquet_Val.data
import com.google.gson.annotations.SerializedName

data class Competicion(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("categoria")
    val categoria: String,

    @SerializedName("temporada")
    val temporada: String,

    @SerializedName("createdAt")
    val createdAt: String? = null
)