package com.munna.kmptesting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed interface Route {
    @Serializable
    data object Login : Route
    @Serializable
    data object Register : Route
    @Serializable
    data object Home : Route
}

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val authViewModel: AuthViewModel = koinViewModel()
            val navController = rememberNavController()

            // Check if user is already logged in for initial screen
            val userName by authViewModel.userName.collectAsState()
            val startDestination: Route = if (userName.isNotEmpty()) Route.Home else Route.Login

            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable<Route.Login> {
                    LoginScreen(
                        viewModel = authViewModel,
                        onBack = {
                            navController.popBackStack()
                        },
                        onNavigateToRegister = {
                            navController.navigate(Route.Register)
                        }
                    )
                }

                composable<Route.Register> {
                    RegisterScreen(
                        viewModel = authViewModel,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<Route.Home> {
                    HomeScreen(
                        viewModel = authViewModel,
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate(Route.Login) {
                                popUpTo<Route.Home> { inclusive = true }
                            }
                        }
                    )
                }
            }

            // Observe login success to navigate to Home
            val isLoginSuccess by authViewModel.isLoginSuccess.collectAsState()
            LaunchedEffect(isLoginSuccess) {
                if (isLoginSuccess) {
                    navController.navigate(Route.Home) {
                        popUpTo<Route.Login> { inclusive = true }
                    }
                    authViewModel.resetLoginSuccess()
                }
            }
        }
    }
}
