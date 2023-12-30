package fr.motoconnect.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import fr.motoconnect.R
import fr.motoconnect.ui.screen.HomeScreen
import fr.motoconnect.ui.screen.journey.JourneysScreen
import fr.motoconnect.ui.screen.MotoScreen
import fr.motoconnect.ui.screen.ProfileScreen
import fr.motoconnect.ui.screen.journey.journeyDetails.JourneyDetailsScreen
import fr.motoconnect.viewmodel.AuthenticationViewModel
import fr.motoconnect.viewmodel.MapViewModel

enum class MotoConnectNavigationRoutes(@DrawableRes val icon: Int, val displayInBar: Boolean) {
    Homepage(icon = R.drawable.homepage, displayInBar = true),
    Journeys(icon = R.drawable.map, displayInBar = true),
    JourneyDetails(icon = R.drawable.map, displayInBar = false),
    Moto(icon = R.drawable.moto, displayInBar = true),
    Profile(icon = R.drawable.profile, displayInBar = true)
}

@Composable
fun MotoConnectNavigation(
    navController: NavHostController = rememberNavController(),
    auth: FirebaseAuth,
    authenticationViewModel: AuthenticationViewModel,
    mapViewModel: MapViewModel
) {

    NavHost(
        navController = navController,
        startDestination = MotoConnectNavigationRoutes.Homepage.name,
    ) {
        composable(MotoConnectNavigationRoutes.Homepage.name) {
            HomeScreen(navController = navController, mapViewModel = mapViewModel)
        }

        composable(MotoConnectNavigationRoutes.Journeys.name) {
            JourneysScreen(navController = navController)
        }

        composable(MotoConnectNavigationRoutes.JourneyDetails.name + "/{journeyId}", arguments = listOf(navArgument("journeyId") { type = NavType.StringType })) {
            JourneyDetailsScreen(it.arguments?.getString("journeyId"), navController = navController)
        }

        composable(MotoConnectNavigationRoutes.Moto.name) {
            MotoScreen()
        }

        composable(MotoConnectNavigationRoutes.Profile.name) {
            ProfileScreen(
                auth = auth,
                authenticationViewModel = authenticationViewModel)
        }
    }

}