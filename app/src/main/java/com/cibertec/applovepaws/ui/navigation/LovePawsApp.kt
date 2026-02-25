package com.cibertec.applovepaws.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cibertec.applovepaws.ui.screens.AdoptionConfirmationScreen
import com.cibertec.applovepaws.ui.screens.AdoptionFormScreen
import com.cibertec.applovepaws.ui.screens.CatalogScreen
import com.cibertec.applovepaws.ui.screens.LoginScreen
import com.cibertec.applovepaws.ui.screens.MyRequestsScreen
import com.cibertec.applovepaws.ui.screens.PetDetailScreen
import com.cibertec.applovepaws.ui.screens.RegisterScreen
import com.cibertec.applovepaws.viewmodel.LovePawsViewModel

@Composable
fun LovePawsApp(viewModel: LovePawsViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = NavRoutes.Login) {
        composable(NavRoutes.Login) {
            LoginScreen(
                onLogin = {
                    viewModel.loadPets()
                    navController.navigate(NavRoutes.Catalog)
                },
                onGoRegister = { navController.navigate(NavRoutes.Register) }
            )
        }
        composable(NavRoutes.Register) {
            RegisterScreen(
                onRegister = {
                    viewModel.loadPets()
                    navController.navigate(NavRoutes.Catalog) {
                        popUpTo(NavRoutes.Login) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.Catalog) {
            CatalogScreen(
                pets = uiState.pets,
                isLoading = uiState.isLoading,
                onSeeDetail = { petId ->
                    viewModel.selectPet(petId)
                    navController.navigate("${NavRoutes.DetailBase}/$petId")
                },
                onAdopt = { petId ->
                    viewModel.selectPet(petId)
                    navController.navigate(NavRoutes.Form)
                },
                onMyRequests = { navController.navigate(NavRoutes.Requests) }
            )
        }
        composable(
            route = NavRoutes.Detail,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId").orEmpty()
            PetDetailScreen(
                pet = uiState.selectedPet?.takeIf { it.id == petId },
                onBack = { navController.popBackStack() },
                onAdopt = { navController.navigate(NavRoutes.Form) }
            )
        }
        composable(NavRoutes.Form) {
            AdoptionFormScreen(
                selectedPet = uiState.selectedPet,
                questions = viewModel.defaultQuestions(),
                onSubmit = { applicant, answers ->
                    viewModel.registerAdoptionRequest(applicant, answers)
                    navController.navigate(NavRoutes.Confirmation)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.Confirmation) {
            AdoptionConfirmationScreen(
                onGoCatalog = {
                    navController.navigate(NavRoutes.Catalog) {
                        popUpTo(NavRoutes.Catalog) { inclusive = true }
                    }
                },
                onViewRequests = { navController.navigate(NavRoutes.Requests) }
            )
        }
        composable(NavRoutes.Requests) {
            MyRequestsScreen(
                requests = uiState.requests,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
