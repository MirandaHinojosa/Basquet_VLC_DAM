package com.Miranda_JC.Basquet_Val.utils

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


//usado para calcular la edad a partir de la fecha de naci
fun calcularEdad(fechaNacimiento: String?): Int? {
    if (fechaNacimiento.isNullOrBlank()) return null

    return try {

        val formatters = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy")
        )

        var fecha: LocalDate? = null
        for (formatter in formatters) {
            try {
                fecha = LocalDate.parse(fechaNacimiento, formatter)
                break
            } catch (e: Exception) {
            }
        }

        if (fecha == null) return null

        val hoy = LocalDate.now()
        Period.between(fecha, hoy).years
    } catch (e: Exception) {
        null
    }
}

// Función auxiliar para formatear fecha
fun formatearFecha(fecha: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = LocalDate.parse(fecha, inputFormatter)
        localDate.format(outputFormatter)
    } catch (e: Exception) {
        fecha
    }
}