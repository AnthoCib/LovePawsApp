package com.cibertec.applovepaws.feature_login.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cibertec.applovepaws.feature_adopcion.ui.AdoptionFormScreen
import com.cibertec.applovepaws.feature_login.LoginViewModel
import com.cibertec.applovepaws.feature_mascota.ui.MascotaDetailScreen
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    val viewModel: LoginViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            MascotaScreen(
                onVerDetalle = { id -> navController.navigate("mascota/$id") },
                onAdoptar = { id -> navController.navigate("adoptar/$id") }
            )
        }

        composable(
            route = "mascota/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            MascotaDetailScreen(mascotaId = backStackEntry.arguments?.getLong("id") ?: 0L)
        }

        composable(
            route = "adoptar/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            AdoptionFormScreen(mascotaId = backStackEntry.arguments?.getLong("id") ?: 0L)
        }
    }
}
