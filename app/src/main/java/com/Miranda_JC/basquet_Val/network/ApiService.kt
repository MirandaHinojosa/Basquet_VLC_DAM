package com.Miranda_JC.Basquet_Val.network

import com.Miranda_JC.Basquet_Val.data.*
import retrofit2.Response
import retrofit2.http.*
import com.google.gson.annotations.SerializedName

/**
 * aquí defino los endpoints disponibles en el backend con sus metodos http
 * cada suspend es una llamada a la api
 */
interface ApiService {
    //llaama a funciones generales de la app

    // Datos generales
    @GET("get_clubes.php")
    suspend fun getClubes(): Response<DataResponse<List<Club>>>

    @GET("get_partidos_en_directo.php")
    suspend fun getPartidosEnDirecto(): Response<DataResponse<List<Partido>>>
    @GET("get_partidos_finalizados.php")
    suspend fun getPartidosFinalizados(): Response<DataResponse<List<Partido>>>
    @GET("get_partidos_programados.php")
    suspend fun getPartidosProgramados(): Response<DataResponse<List<Partido>>>

    @GET("get_competiciones.php")
    suspend fun getCompeticiones(): Response<DataResponse<List<Competicion>>>

    @GET("get_clasificacion.php")
    suspend fun getClasificacion(
        @Query("competicion_id") competicionId: Int
    ): Response<DataResponse<List<Clasificacion>>>

    @GET("get_jugadores.php")
    suspend fun getJugadores(
        @Query("equipo") equipo: String = "",
        @Query("equipo_id") equipoId: Int? = null
    ): Response<DataResponse<List<Jugador>>>


    @GET("get_club_detalle.php")
    suspend fun getClubDetalle(
        @Query("club_id") clubId: Int
    ): Response<DataResponse<ClubDetalle>>

    @GET("get_competicion_detalle.php")
    suspend fun getCompeticionDetalle(
        @Query("competicion_id") competicionId: Int
    ): Response<DataResponse<CompeticionDetalle>>

    @GET("get_jugador_detalle.php")
    suspend fun getJugadorDetalle(
        @Query("jugador_id") jugadorId: Int
    ): Response<DataResponse<JugadorDetalle>>

    @GET("get_partido_detalle.php")
    suspend fun getPartidoDetalle(
        @Query("partido_id") partidoId: Int
    ): Response<DataResponse<PartidoDetalle>>


    @GET("get_estadisticas_partido.php")
    suspend fun getEstadisticasPartido(
        @Query("partido_id") partidoId: Int
    ): Response<DataResponse<List<Estadistica>>>

    // Equipos
    @GET("get_equipos.php")
    suspend fun getEquipos(
        @Query("club_id") clubId: Int? = null,
        @Query("categoria") categoria: String? = null
    ): Response<DataResponse<List<Equipo>>>
}


data class LoginRequest(
    val usuario: String,
    val password: String
)

data class RegisterRequest(
    val usuario: String,
    val email: String,
    val password: String
)

// Response models para detalles
data class ClubDetalle(
    val club: Club,
    val equipos: List<Equipo> = emptyList()
)

data class CompeticionDetalle(
    val competicion: Competicion,
    val clasificacion: List<Clasificacion> = emptyList(),
    val partidos: List<Partido> = emptyList()
)

data class JugadorDetalle(
    val jugador: Jugador,
    val estadisticas: EstadisticasJugador? = null,
    val partidosJugados: Int = 0
)

data class EstadisticasJugador(
    val partidosJugados: Int = 0,
    val puntosPorPartido: Float = 0f,
    val rebotesPorPartido: Float = 0f,
    val asistenciasPorPartido: Float = 0f,
    val eficienciaPorPartido: Float = 0f
)

data class PartidoDetalle(
    val partido: Partido,
    val estadisticas: List<Estadistica> = emptyList(),
    val alineaciones: Map<String, List<Jugador>> = emptyMap()
)

// Extension function para manejo seguro de llamadas API
suspend fun <T> safeApiCall(apiCall: suspend () -> Response<DataResponse<T>>): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true) {
                body.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No data available"))
            } else {
                Result.failure(Exception(body?.message ?: "Unknown error"))
            }
        } else {
            Result.failure(Exception("HTTP Error: ${response.code()} - ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}