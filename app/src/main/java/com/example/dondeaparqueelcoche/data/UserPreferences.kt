package com.example.dondeaparqueelcoche.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear el DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Gestor de preferencias del usuario usando DataStore
 * Guarda y recupera el nombre del usuario seleccionado
 */
class UserPreferences(private val context: Context) {
    
    companion object {
        private val SELECTED_USER_KEY = stringPreferencesKey("selected_user")
    }
    
    /**
     * Flow que emite el usuario seleccionado actualmente
     * Retorna null si no hay usuario guardado
     */
    val selectedUserFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_USER_KEY]
        }
    
    /**
     * Guarda el usuario seleccionado
     * @param userName Nombre del usuario a guardar
     */
    suspend fun saveSelectedUser(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_USER_KEY] = userName
        }
    }
    
    /**
     * Borra el usuario guardado (para cerrar sesión)
     */
    suspend fun clearSelectedUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(SELECTED_USER_KEY)
        }
    }
}
