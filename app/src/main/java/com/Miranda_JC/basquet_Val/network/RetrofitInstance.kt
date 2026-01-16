package com.Miranda_JC.Basquet_Val.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
/**
 * el singleton  crea y gestiona la conexión con la API del servidor
 */
object RetrofitInstance {
    //url a la api
    private const val BASE_URL = "https://gets-app.onrender.com/api/"

    //importante para depurar los errores con LogCAT
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Client con timeout configurado
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    //creamos Gson para conver json a objetos kotlin
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .serializeNulls()
        .create()

    // Instancia de Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Instancia del servicio API
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }


    fun updateBaseUrl(newBaseUrl: String): ApiService {
        val newRetrofit = Retrofit.Builder()
            .baseUrl(newBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return newRetrofit.create(ApiService::class.java)
    }
}
