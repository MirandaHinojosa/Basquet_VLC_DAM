package com.Miranda_JC.Basquet_Val.data

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String? = null,
    val createdAt: String? = null
)

data class LoginRequest(val usuario: String, val password: String)
data class RegisterRequest(val username: String, val email: String, val password: String)
data class LoginResponse(val success: Boolean, val message: String?, val user: User?)
data class ApiResponse(val success: Boolean, val message: String)