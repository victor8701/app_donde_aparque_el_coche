package com.example.dondeaparqueelcoche.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * ⚠️ IMPORTANTE: Esta es la URL de tu servidor en la nube (Render)
 * Funciona 24/7 sin necesidad de tener el ordenador encendido
 */
private const val BASE_URL = "https://app-donde-aparque-el-coche.onrender.com/"

/**
 * Interfaz Retrofit para las llamadas HTTP al servidor
 */
interface ApiService {
    
    /**
     * Obtener el estado actual del aparcamiento
     */
    @GET("status")
    suspend fun getStatus(): Response<ParkingStatus>
    
    /**
     * Actualizar el estado del aparcamiento
     */
    @POST("status")
    suspend fun updateStatus(@Body status: ParkingStatus): Response<ApiResponse>
    
    companion object {
        /**
         * Crear instancia del servicio
         */
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            return retrofit.create(ApiService::class.java)
        }
    }
}

/**
 * Respuesta del servidor al actualizar
 */
data class ApiResponse(
    val success: Boolean,
    val data: ParkingStatus?
)
