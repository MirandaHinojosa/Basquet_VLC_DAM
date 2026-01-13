package com.Miranda_JC.Basquet_Val.network

import com.google.gson.annotations.SerializedName

/**
 * modelo generico para toda slas respuesta la api
 */
data class ApiResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null
)
// lo mism pero para el login.. añadiendo USer para los datos del usuario
data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("user") val user: UserResponse? = null
)
//respuesta generica para datos, es la más utilizada por los endpoints para traer datos.
data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("nombre_completo") val nombreCompleto: String? = null
)

data class DataResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T? = null,
    @SerializedName("message") val message: String? = null
)