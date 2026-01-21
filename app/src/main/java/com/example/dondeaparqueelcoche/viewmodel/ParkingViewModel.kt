package com.example.dondeaparqueelcoche.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dondeaparqueelcoche.data.ParkingRepository
import com.example.dondeaparqueelcoche.data.ParkingStatus
import com.example.dondeaparqueelcoche.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados posibles de la UI
 */
sealed class UiState {
    object Loading : UiState()
    data class Success(val parkingStatus: ParkingStatus?) : UiState()
    data class Error(val message: String) : UiState()
}

/**
 * ViewModel que gestiona el estado de la aplicación
 * Coordina entre UserPreferences, ParkingRepository y las pantallas de UI
 */
class ParkingViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userPreferences = UserPreferences(application)
    private val parkingRepository = ParkingRepository()
    
    // Estado del usuario seleccionado
    private val _selectedUser = MutableStateFlow<String?>(null)
    val selectedUser: StateFlow<String?> = _selectedUser.asStateFlow()
    
    // Estado del aparcamiento
    private val _parkingState = MutableStateFlow<UiState>(UiState.Loading)
    val parkingState: StateFlow<UiState> = _parkingState.asStateFlow()
    
    // Estado para mostrar mensajes de feedback
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()
    
    init {
        // Cargar usuario guardado
        loadSelectedUser()
        // Escuchar cambios en Firebase
        observeParkingStatus()
    }
    
    /**
     * Carga el usuario seleccionado desde DataStore
     */
    private fun loadSelectedUser() {
        viewModelScope.launch {
            userPreferences.selectedUserFlow.collect { userName ->
                _selectedUser.value = userName
            }
        }
    }
    
    /**
     * Observa los cambios en tiempo real del estado del aparcamiento
     */
    private fun observeParkingStatus() {
        viewModelScope.launch {
            parkingRepository.getParkingStatusFlow().collect { status ->
                _parkingState.value = UiState.Success(status)
            }
        }
    }
    
    /**
     * Guarda el usuario seleccionado
     * @param userName Nombre del usuario
     */
    fun selectUser(userName: String) {
        viewModelScope.launch {
            userPreferences.saveSelectedUser(userName)
            _selectedUser.value = userName
        }
    }
    
    /**
     * Actualiza la ubicación del coche
     * @param location Nueva ubicación
     */
    fun updateLocation(location: String) {
        val userName = _selectedUser.value
        if (userName == null) {
            _feedbackMessage.value = "Error: no hay usuario seleccionado"
            return
        }
        
        viewModelScope.launch {
            val success = parkingRepository.updateParkingLocation(location, userName)
            _feedbackMessage.value = if (success) {
                "✅ Ubicación actualizada"
            } else {
                "❌ Error al actualizar"
            }
            
            // Limpiar mensaje después de mostrarlo
            kotlinx.coroutines.delay(2000)
            _feedbackMessage.value = null
        }
    }
    
    /**
     * Cierra sesión del usuario actual
     */
    fun logout() {
        viewModelScope.launch {
            userPreferences.clearSelectedUser()
            _selectedUser.value = null
        }
    }
    
    /**
     * Limpia el mensaje de feedback
     */
    fun clearFeedbackMessage() {
        _feedbackMessage.value = null
    }
}
