package com.Miranda_JC.Basquet_Val.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Miranda_JC.Basquet_Val.data.RegisterRequest
import java.util.Date

@Entity(tableName = "usuarios")
data class UsuarioLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String, // Encriptar en producción
    val fechaRegistro: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromRegisterRequest(request: RegisterRequest): UsuarioLocal {
            return UsuarioLocal(
                username = request.username,
                email = request.email,
                password = request.password // ¡En producción deberías usar BCrypt!
            )
        }
    }
}
