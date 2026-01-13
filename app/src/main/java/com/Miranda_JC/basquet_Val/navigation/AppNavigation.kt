package com.Miranda_JC.Basquet_Val.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.Miranda_JC.Basquet_Val.screens.auth.*
import com.Miranda_JC.Basquet_Val.screens.detail.*
import com.Miranda_JC.Basquet_Val.screens.main.*
import com.Miranda_JC.Basquet_Val.screens.profile.ProfileScreen
import com.Miranda_JC.Basquet_Val.viewmodels.AuthViewModel
import com.Miranda_JC.Basquet_Val.viewmodels.ViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Pantallas de autenticación
        composable("splash") { SplashScreen(navController) }

        composable("login") {

            val context = LocalContext.current
            val authViewModel: AuthViewModel = viewModel(
                factory = ViewModelFactory.provideFactory(context)
            )
            LoginScreen(navController, authViewModel)
        }
        composable("perfil") {
            ProfileScreen(navController = navController)
        }
        composable("register") { RegisterScreen(navController) }

        //Pantallas principales de la app
        composable("main") { MainScreen(navController) }
        composable("partidos") { PartidosScreen(navController) }
        composable("competiciones") { CompeticionesScreen(navController) }
        composable("clubes") { ClubesScreen(navController) }
        composable("jugadores") { JugadoresScreen(navController) }

        //Pantallas de detalle con parámetros
        //tdos son similares.
        composable(
            route = "partido_detalle/{partidoId}",
            arguments = listOf(navArgument("partidoId") { type = androidx.navigation.NavType.IntType })
        ) { backStackEntry ->
            val partidoId = backStackEntry.arguments?.getInt("partidoId") ?: 0
            PartidoDetalleScreen(navController, partidoId)
        }

        composable(
            route = "club_detalle/{clubId}",
            arguments = listOf(navArgument("clubId") { type = androidx.navigation.NavType.IntType })
        ) { backStackEntry ->
            val clubId = backStackEntry.arguments?.getInt("clubId") ?: 0
            ClubDetalleScreen(navController, clubId)
        }

        composable(
            route = "competicion_detalle/{competicionId}",
            arguments = listOf(navArgument("competicionId") { type = androidx.navigation.NavType.IntType })
        ) { backStackEntry ->
            val competicionId = backStackEntry.arguments?.getInt("competicionId") ?: 0
            CompeticionDetalleScreen(navController, competicionId)
        }

        // Añade esto para jugador_detalle
        composable(
            route = "jugador_detalle/{jugadorId}",
            arguments = listOf(navArgument("jugadorId") { type = androidx.navigation.NavType.IntType })
        ) { backStackEntry ->
            val jugadorId = backStackEntry.arguments?.getInt("jugadorId") ?: 0
            JugadorDetalleScreen(navController, jugadorId)
        }
    }
}

// Extension function para facilitar la navegación
fun NavHostController.navigateToMain() = navigate("main")
fun NavHostController.navigateToLogin() = navigate("login")
fun NavHostController.navigateToRegister() = navigate("register")
fun NavHostController.navigateToClubDetail(clubId: Int) = navigate("club_detalle/$clubId")

fun NavHostController.navigateToPartidoDetail(partidoId: Int) = navigate("partido_detalle/$partidoId")
fun NavHostController.navigateToCompeticionDetail(competicionId: Int) = navigate("competicion_detalle/$competicionId")
fun NavHostController.navigateToJugadorDetail(jugadorId: Int) = navigate("jugador_detalle/$jugadorId")

