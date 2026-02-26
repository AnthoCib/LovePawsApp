package com.cibertec.applovepaws.feature_login.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModelFactory
import com.cibertec.applovepaws.feature_adopcion.ui.SolicitudScreen
import com.cibertec.applovepaws.feature_login.LoginViewModel
import com.cibertec.applovepaws.feature_login.LoginViewModelFactory
import com.cibertec.applovepaws.feature_login.UserSession
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.ui.MascotaDetalleScreen
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))
    val mascotaViewModel: MascotaViewModel = viewModel()
    val solicitudViewModel: SolicitudViewModel = viewModel(factory = SolicitudViewModelFactory())

    var session by remember { mutableStateOf<UserSession?>(null) }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = { user ->
                    session = UserSession(
                        id = user.id.toInt(),
                        name = user.name,
                        email = user.email
                    )
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = loginViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            val current = session
            if (current != null) {
                HomeScreen(
                    session = current,
                    onIrCatalogo = { navController.navigate("catalogo") }
                )
            }
        }

        composable("catalogo") {
            MascotaScreen(
                viewModel = mascotaViewModel,
                onVerDetalle = { mascotaId ->
                    navController.navigate("detalle/$mascotaId")
                },
                onAdoptar = { mascotaId ->
                    navController.navigate("solicitud/$mascotaId")
                }
            )
        }

        composable(
            route = "detalle/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            val mascota = mascotaViewModel.obtenerMascotaPorId(mascotaId)
            MascotaDetalleScreen(
                mascota = mascota,
                onAdoptar = { navController.navigate("solicitud/$mascotaId") }
            )
        }

        composable(
            route = "solicitud/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            val current = session
            if (current != null) {
                SolicitudScreen(
                    viewModel = solicitudViewModel,
                    usuarioId = current.id,
                    mascotaId = mascotaId,
                    userName = current.name,
                    userEmail = current.email
                )
            }
        }
    }
}
