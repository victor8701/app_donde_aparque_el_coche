package com.example.dondeaparqueelcoche.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dondeaparqueelcoche.data.ParkingStatus
import com.example.dondeaparqueelcoche.viewmodel.UiState

/**
 * Pantalla principal del dashboard
 * Muestra el estado actual del coche y botones para actualizar la ubicación
 */
@Composable
fun ParkingDashboardScreen(
    uiState: UiState,
    currentUser: String,
    onUpdateLocation: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCustomLocationDialog by remember { mutableStateOf(false) }
    
    val predefinedLocations = listOf(
        "Óbolo (farmacia)" to "💊",
        "Óbolo (guardería)" to "🧸",
        "C/ Dobla" to "📍",
        "Petroprix" to "⛽",
        "Ballenoil" to "⛽",
        "Mercadona" to "🛒"
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Estado del coche
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(32.dp)
                )
            }
            
            is UiState.Success -> {
                ParkingStatusCard(
                    parkingStatus = uiState.parkingStatus,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            is UiState.Error -> {
                ErrorCard(
                    message = uiState.message,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Título de actualización
        Text(
            text = "Actualizar ubicación",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Grid de botones de ubicación
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(predefinedLocations) { (location, emoji) ->
                LocationButton(
                    location = location,
                    emoji = emoji,
                    onClick = { onUpdateLocation(location) }
                )
            }
            
            // Botón "Otro"
            item {
                LocationButton(
                    location = "Otro...",
                    emoji = "✏️",
                    onClick = { showCustomLocationDialog = true },
                    isSpecial = true
                )
            }
        }
        
        // Botón de logout
        TextButton(
            onClick = onLogout,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cambiar usuario")
        }
    }
    
    // Dialog para ubicación personalizada
    if (showCustomLocationDialog) {
        CustomLocationDialog(
            onDismiss = { showCustomLocationDialog = false },
            onConfirm = { location ->
                onUpdateLocation(location)
                showCustomLocationDialog = false
            }
        )
    }
}

/**
 * Tarjeta que muestra el estado actual del aparcamiento
 */
@Composable
fun ParkingStatusCard(
    parkingStatus: ParkingStatus?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "El coche está en:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Text(
                text = parkingStatus?.location ?: "Desconocido",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
            
            if (parkingStatus != null && parkingStatus.user.isNotEmpty()) {
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Aparcado por ${parkingStatus.user}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Actualizado: ${parkingStatus.timestamp}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

/**
 * Botón de ubicación con emoji
 */
@Composable
fun LocationButton(
    location: String,
    emoji: String,
    onClick: () -> Unit,
    isSpecial: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSpecial) 
                MaterialTheme.colorScheme.tertiaryContainer 
            else 
                MaterialTheme.colorScheme.secondaryContainer,
            contentColor = if (isSpecial) 
                MaterialTheme.colorScheme.onTertiaryContainer 
            else 
                MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Dialog para ingresar ubicación personalizada
 */
@Composable
fun CustomLocationDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var customLocation by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva ubicación") },
        text = {
            Column {
                Text("Escribe dónde has aparcado:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customLocation,
                    onValueChange = { customLocation = it },
                    placeholder = { Text("Ej: Calle Gran Vía, 12") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (customLocation.isNotBlank()) {
                        onConfirm(customLocation)
                    }
                },
                enabled = customLocation.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Tarjeta de error
 */
@Composable
fun ErrorCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
