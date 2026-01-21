package com.example.dondeaparqueelcoche.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de selección de usuario
 * Muestra botones grandes para que cada persona seleccione su nombre
 */
@Composable
fun UserSelectionScreen(
    onUserSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val users = listOf(
        "Víctor" to "👨‍💻",
        "Celia" to "👩‍⚕️",
        "Chema" to "👮‍♂️",
        "Yoli" to "👩‍🏫"
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono del coche
        Text(
            text = "🚙",
            fontSize = 72.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "Coche Compartido",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "¿Quién eres?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
            textAlign = TextAlign.Center
        )
        
        // Botones de usuario
        users.forEach { (name, emoji) ->
            UserButton(
                userName = name,
                emoji = emoji,
                onClick = { onUserSelected(name) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}

/**
 * Botón individual para seleccionar un usuario
 */
@Composable
fun UserButton(
    userName: String,
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = emoji,
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
