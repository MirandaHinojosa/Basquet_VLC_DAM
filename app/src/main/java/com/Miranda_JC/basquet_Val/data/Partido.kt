package com.Miranda_JC.Basquet_Val.data

import com.google.gson.annotations.SerializedName

data class Partido(
    @SerializedName("id")
    val id: Int,

    @SerializedName("competicion_id")
    val competicionId: Int,

    @SerializedName("equipo_local_id")
    val equipoLocalId: Int,

    @SerializedName("equipo_visitante_id")
    val equipoVisitanteId: Int,

    @SerializedName("fecha_hora")
    val fechaHora: String,

    @SerializedName("pabellon")
    val pabellon: String? = null,

    @SerializedName("estado")
    val estado: String, // PROGRAMADO, EN_CURSO, FINALIZADO

    @SerializedName("resultado_local")
    val resultadoLocal: Int? = null,

    @SerializedName("resultado_visitante")
    val resultadoVisitante: Int? = null,

    @SerializedName("fase")
    val fase: String? = null,

    @SerializedName("grupo")
    val grupo: String? = null,

    @SerializedName("arbitro_principal")
    val arbitroPrincipal: String? = null,

    @SerializedName("equipo_local_nombre")
    val equipoLocalNombre: String? = null,

    @SerializedName("equipo_visitante_nombre")
    val equipoVisitanteNombre: String? = null,

    @SerializedName("competicion_nombre")
    val competicionNombre: String? = null,

    @SerializedName("direccion_pabellon")
    val direccionPabellon: String? = null
)