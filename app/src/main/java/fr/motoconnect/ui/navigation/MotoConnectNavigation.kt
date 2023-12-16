package fr.motoconnect.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.motoconnect.R
import fr.motoconnect.data.service.GoogleAuthUiClient
import fr.motoconnect.ui.screen.HomeScreen
import fr.motoconnect.ui.screen.JourneysScreen
import fr.motoconnect.ui.screen.MotoScreen
import fr.motoconnect.ui.screen.ProfileScreen

enum class MotoConnectNavigationRoutes(@DrawableRes val icon: Int) {
    Homepage(icon = R.drawable.homepage),
    Journeys(icon = R.drawable.map),
    Moto(R.drawable.moto),
    Profile(icon = R.drawable.profile)
}

@Composable
fun MotoConnectNavigation(
    navController: NavHostController = rememberNavController(),
    googleAuthUiClient: GoogleAuthUiClient,
) {

    NavHost(
        navController = navController,
        startDestination = MotoConnectNavigationRoutes.Homepage.name,
    ) {
        composable(MotoConnectNavigationRoutes.Homepage.name) {
            HomeScreen(navController = navController)
        }

        composable(MotoConnectNavigationRoutes.Journeys.name) {
            JourneysScreen()
        }

        composable(MotoConnectNavigationRoutes.Moto.name) {
            MotoScreen()
        }

        composable(MotoConnectNavigationRoutes.Profile.name) {
            ProfileScreen(userData = googleAuthUiClient.getSignedInUser())
        }
    }

}