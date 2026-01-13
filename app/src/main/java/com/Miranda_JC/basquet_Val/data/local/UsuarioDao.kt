package com.Miranda_JC.Basquet_Val.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(usuario: UsuarioLocal): Long

    @Query("SELECT * FROM usuarios WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): UsuarioLocal?

    @Query("SELECT * FROM usuarios WHERE username = :username")
    suspend fun getUsuarioByUsername(username: String): UsuarioLocal?

    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun getUsuarioByEmail(email: String): UsuarioLocal?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUsuarioById(id: Int): Flow<UsuarioLocal?>

    @Query("SELECT COUNT(*) FROM usuarios WHERE username = :username OR email = :email")
    suspend fun checkUsuarioExists(username: String, email: String): Int
}

