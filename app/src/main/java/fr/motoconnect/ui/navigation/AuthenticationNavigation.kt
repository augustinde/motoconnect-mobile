package fr.motoconnect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.motoconnect.ui.screen.authentication.ResetPasswordScreen
import fr.motoconnect.ui.screen.authentication.SignInScreen
import fr.motoconnect.ui.screen.authentication.SignUpScreen
import fr.motoconnect.viewmodel.AuthenticationViewModel


enum class AuthenticationNavigationRoutes {
    SignIn,
    SignUp,
    ResetPassword
}

@Composable
fun AuthenticationNavigation(
    authenticationViewModel: AuthenticationViewModel,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthenticationNavigationRoutes.SignIn.name,
    ) {
        composable(route = AuthenticationNavigationRoutes.SignIn.name) {
            SignInScreen(
                authenticationViewModel = authenticationViewModel,
                navController = navController
            )
        }
        composable(route = AuthenticationNavigationRoutes.SignUp.name) {
            SignUpScreen(
                authenticationViewModel = authenticationViewModel,
                navController = navController
            )
        }
        composable(route = AuthenticationNavigationRoutes.ResetPassword.name) {
            ResetPasswordScreen(
                authenticationViewModel = authenticationViewModel,
                navController = navController
            )
        }
    }
}