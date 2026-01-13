package com.Miranda_JC.Basquet_Val.data

data class Estadistica(
    val id: Int,
    val partidoId: Int,
    val jugadorId: Int,
    val puntos: Int = 0,
    val rebotes: Int = 0,
    val asistencias: Int = 0,
    val robos: Int = 0,
    val tapones: Int = 0,
    val perdidas: Int = 0, // Nuevo campo
    val faltas: Int = 0,
    val minutosJugados: Float? = null,
    val jugadorNombre: String? = null,
    val jugadorApellidos: String? = null,
    val numeroCamiseta: Int? = null,
    val tirosLibresIntentados: Int? = null,
    val tirosLibresAcertados: Int? = null,
    val tirosDosIntentados: Int? = null,
    val tirosDosAcertados: Int? = null,
    val tirosTresIntentados: Int? = null,
    val tirosTresAcertados: Int? = null
) {

    val porcentajeTL: Float
        get() = if ((tirosLibresIntentados ?: 0) > 0) {
            ((tirosLibresAcertados?.toFloat() ?: 0f) / (tirosLibresIntentados ?: 1) * 100)
        } else 0f

    val porcentajeT2: Float
        get() = if ((tirosDosIntentados ?: 0) > 0) {
            ((tirosDosAcertados?.toFloat() ?: 0f) / (tirosDosIntentados ?: 1) * 100)
        } else 0f

    val porcentajeT3: Float
        get() = if ((tirosTresIntentados ?: 0) > 0) {
            ((tirosTresAcertados?.toFloat() ?: 0f) / (tirosTresIntentados ?: 1) * 100)
        } else 0f

    val eficiencia: Int
        get() = puntos + rebotes + asistencias + robos + tapones - perdidas - faltas
}