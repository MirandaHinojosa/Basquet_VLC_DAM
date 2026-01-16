package com.Miranda_JC.Basquet_Val.data

import com.google.gson.annotations.SerializedName

data class Clasificacion(
    @SerializedName("id") val id: Int,
    @SerializedName("competicion_id") val competicionId: Int,
    @SerializedName("equipo_id") val equipoId: Int,
    @SerializedName("posicion") val posicion: Int,
    @SerializedName("partidos_jugados") val partidosJugados: Int,
    @SerializedName("partidos_ganados") val partidosGanados: Int,
    @SerializedName("partidos_perdidos") val partidosPerdidos: Int,
    @SerializedName("puntos_clasificacion") val puntosClasificacion: Int,
    @SerializedName("equipo_nombre") val equipoNombre: String? = null,
    @SerializedName("club_nombre") val clubNombre: String? = null,
    @SerializedName("puntos_favor") val puntosFavor: Int? = null,
    @SerializedName("puntos_contra") val puntosContra: Int? = null
)
