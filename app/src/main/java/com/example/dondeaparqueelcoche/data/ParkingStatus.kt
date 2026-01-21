package com.example.dondeaparqueelcoche.data

/**
 * Modelo de datos que representa el estado del aparcamiento del coche
 * @param location Ubicación donde está aparcado el coche
 * @param user Usuario que aparcó el coche
 * @param timestamp Marca de tiempo en formato legible (ej: "19:30 19 ene")
 * @param timestampRaw Marca de tiempo en milisegundos para comparaciones
 */
data class ParkingStatus(
    val location: String = "Desconocido",
    val user: String = "",
    val timestamp: String = "",
    val timestampRaw: Long = 0L
)
