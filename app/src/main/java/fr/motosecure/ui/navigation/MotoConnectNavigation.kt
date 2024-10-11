package fr.motosecure.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import fr.motosecure.R
import fr.motosecure.ui.screen.home.HomeScreen
import fr.motosecure.ui.screen.journey.journeyDetails.JourneyDetailsScreen
import fr.motosecure.ui.screen.journey.journeyList.JourneyListScreen
import fr.motosecure.ui.screen.moto.MotoScreen
import fr.motosecure.ui.screen.settings.SettingsScreen
import fr.motosecure.viewmodel.MapViewModel

enum class MotoConnectNavigationRoutes(@DrawableRes val icon: Int, val displayInBar: Boolean) {
    Homepage(icon = R.drawable.home, displayInBar = true),
    Journeys(icon = R.drawable.journeys, displayInBar = true),
    JourneyDetails(icon = R.drawable.journeys, displayInBar = false),
    Moto(icon = R.drawable.moto, displayInBar = true),
    Settings(icon = R.drawable.settings, displayInBar = true)
}

@Composable
fun MotoConnectNavigation(
    navController: NavHostController = rememberNavController(),
    mapViewModel: MapViewModel
) {

    val uri = "https://motoconnect.com"

    NavHost(
        navController = navController,
        startDestination = MotoConnectNavigationRoutes.Homepage.name,

        ) {
        composable(MotoConnectNavigationRoutes.Homepage.name) {
            HomeScreen(
                mapViewModel = mapViewModel
            )
        }

        composable(MotoConnectNavigationRoutes.Journeys.name) {
            JourneyListScreen(
                navController = navController,
            )
        }

        composable(
            MotoConnectNavigationRoutes.JourneyDetails.name + "/{journeyId}",
            arguments = listOf(navArgument("journeyId") { type = NavType.StringType })
        ) {
            JourneyDetailsScreen(
                it.arguments?.getString("journeyId"),
                navController = navController,
            )
        }

        composable(MotoConnectNavigationRoutes.Moto.name, deepLinks = listOf(navDeepLink { uriPattern = "$uri/moto" })) {
            MotoScreen()
        }

        composable(MotoConnectNavigationRoutes.Settings.name) {
            SettingsScreen()
        }
    }

}