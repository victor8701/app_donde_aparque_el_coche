package com.example.dondeaparqueelcoche.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio para interactuar con el servidor HTTP
 * Gestiona la lectura y escritura del estado del aparcamiento
 */
class ParkingRepository {
    
    private val apiService = ApiService.create()
    
    /**
     * Flow que emite actualizaciones del estado del aparcamiento
     * Hace polling cada 3 segundos para obtener actualizaciones
     */
    fun getParkingStatusFlow(): Flow<ParkingStatus?> = flow {
        while (true) {
            try {
                val response = apiService.getStatus()
                if (response.isSuccessful) {
                    emit(response.body())
                } else {
                    emit(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(null)
            }
            
            // Esperar 3 segundos antes de la siguiente consulta
            delay(3000)
        }
    }
    
    /**
     * Actualiza la ubicación del coche en el servidor
     * @param location Nueva ubicación del coche
     * @param userName Usuario que está actualizando la ubicación
     * @return true si se guardó correctamente, false si hubo error
     */
    suspend fun updateParkingLocation(location: String, userName: String): Boolean {
        return try {
            val now = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("HH:mm dd MMM", Locale("es", "ES"))
            val timeString = dateFormat.format(Date(now))
            
            val parkingStatus = ParkingStatus(
                location = location,
                user = userName,
                timestamp = timeString,
                timestampRaw = now
            )
            
            val response = apiService.updateStatus(parkingStatus)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
