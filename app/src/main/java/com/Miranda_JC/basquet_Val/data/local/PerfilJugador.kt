package com.Miranda_JC.Basquet_Val.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "perfiles_jugador",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioLocal::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PerfilJugador(
    @PrimaryKey
    val usuarioId: Int,
    val nombreCompleto: String = "",
    val fechaNacimiento: String? = null,
    val posicion: String? = null,
    val equipoFavorito: String? = null,
    val fotoPerfil: String? = null,
    val telefono: String? = null,
    val ubicacion: String? = null,
    val bio: String? = null,
    val ultimaActualizacion: Long = System.currentTimeMillis()
)