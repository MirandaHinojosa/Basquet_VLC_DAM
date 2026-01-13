package com.Miranda_JC.Basquet_Val.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilJugadorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(perfil: PerfilJugador)

    @Update
    suspend fun update(perfil: PerfilJugador)

    @Query("SELECT * FROM perfiles_jugador WHERE usuarioId = :usuarioId")
    fun getPerfilByUsuarioId(usuarioId: Int): Flow<PerfilJugador?>

    @Query("DELETE FROM perfiles_jugador WHERE usuarioId = :usuarioId")
    suspend fun deleteByUsuarioId(usuarioId: Int)
}