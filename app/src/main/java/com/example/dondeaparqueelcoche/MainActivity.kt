package com.example.dondeaparqueelcoche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dondeaparqueelcoche.ui.screens.ParkingDashboardScreen
import com.example.dondeaparqueelcoche.ui.screens.UserSelectionScreen
import com.example.dondeaparqueelcoche.ui.theme.DondeAparqueElCocheTheme
import com.example.dondeaparqueelcoche.viewmodel.ParkingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DondeAparqueElCocheTheme {
                MainScreen()
            }
        }
    }
}

/**
 * Pantalla principal que maneja la navegación entre selección de usuario y dashboard
 */
@Composable
fun MainScreen(
    viewModel: ParkingViewModel = viewModel()
) {
    val selectedUser by viewModel.selectedUser.collectAsState()
    val parkingState by viewModel.parkingState.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    
    // Mostrar Snackbar si hay mensaje de feedback
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearFeedbackMessage()
        }
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        // Navegación simple: si hay usuario, mostrar dashboard; si no, mostrar selección
        if (selectedUser != null) {
            ParkingDashboardScreen(
                uiState = parkingState,
                currentUser = selectedUser!!,
                onUpdateLocation = { location ->
                    viewModel.updateLocation(location)
                },
                onLogout = {
                    viewModel.logout()
                },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            UserSelectionScreen(
                onUserSelected = { userName ->
                    viewModel.selectUser(userName)
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}