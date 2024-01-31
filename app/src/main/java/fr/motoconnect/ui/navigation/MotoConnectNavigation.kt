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
import fr.motoconnect.ui.screen.home.HomeScreen
import fr.motoconnect.ui.screen.journey.journeyDetails.JourneyDetailsScreen
import fr.motoconnect.ui.screen.journey.journeyList.JourneyListScreen
import fr.motoconnect.ui.screen.moto.MotoScreen
import fr.motoconnect.ui.screen.profile.ModifyProfileScreen
import fr.motoconnect.ui.screen.profile.ProfileScreen
import fr.motoconnect.viewmodel.AuthenticationViewModel
import fr.motoconnect.viewmodel.MapViewModel

enum class MotoConnectNavigationRoutes(@DrawableRes val icon: Int, val displayInBar: Boolean) {
    Homepage(icon = R.drawable.home, displayInBar = true),
    Journeys(icon = R.drawable.journeys, displayInBar = true),
    JourneyDetails(icon = R.drawable.journeys, displayInBar = false),
    Moto(icon = R.drawable.moto, displayInBar = true),
    Profile(icon = R.drawable.profile, displayInBar = true)
}

enum class ProfileNavigationRoutes {
    ModifyProfile,
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
            HomeScreen(mapViewModel = mapViewModel)
        }

        composable(MotoConnectNavigationRoutes.Journeys.name) {
            JourneyListScreen(navController = navController)
        }

        composable(
            MotoConnectNavigationRoutes.JourneyDetails.name + "/{journeyId}",
            arguments = listOf(navArgument("journeyId") { type = NavType.StringType })
        ) {
            JourneyDetailsScreen(
                it.arguments?.getString("journeyId"), navController = navController
            )
        }

        composable(MotoConnectNavigationRoutes.Moto.name) {
            MotoScreen()
        }

        composable(MotoConnectNavigationRoutes.Profile.name) {
            ProfileScreen(
                auth = auth, authenticationViewModel = authenticationViewModel, navController = navController
            )
        }
        composable(route = ProfileNavigationRoutes.ModifyProfile.name) {
            ModifyProfileScreen(authenticationViewModel = authenticationViewModel, navController = navController, auth = auth)
        }
    }

}