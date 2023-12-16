package fr.motoconnect

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import fr.motoconnect.data.service.GoogleAuthUiClient
import fr.motoconnect.ui.navigation.MotoConnectNavigation
import fr.motoconnect.ui.screen.HomeScreen
import fr.motoconnect.ui.screen.MapScreen
import fr.motoconnect.ui.screen.MotoScreen
import fr.motoconnect.ui.screen.ProfileScreen
import fr.motoconnect.ui.screen.SignInScreen
import fr.motoconnect.ui.theme.MotoConnectTheme
import fr.motoconnect.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoConnectTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val viewModel = viewModel<AuthViewModel>()
                val state by viewModel.state.collectAsState()

                if(googleAuthUiClient.getSignedInUser() != null) {
                    MainScreen(
                        currentDestination = currentDestination,
                        navController = navController,
                        googleAuthUiClient = googleAuthUiClient,
                        onSignout = {
                            lifecycleScope.launch {
                                googleAuthUiClient.signOut()
                                Toast.makeText(
                                    applicationContext,
                                    applicationContext.getText(R.string.sign_out_success),
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.popBackStack()
                            }
                        }
                    )
                }else{
                    LaunchedEffect(key1 = Unit) {
                        if(googleAuthUiClient.getSignedInUser() != null) {
                            navController.navigate(MotoConnectNavigation.Homepage.name)
                        }
                    }

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if(result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    viewModel.onSignInResult(signInResult)
                                }
                            }
                        }
                    )

                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                        if(state.isSignInSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                applicationContext.getText(R.string.sign_in_success),
                                Toast.LENGTH_LONG
                            ).show()

                            navController.navigate(MotoConnectNavigation.Homepage.name)
                            viewModel.resetState()
                        }
                    }

                    SignInScreen(
                        state = state,
                        onSignInClick = {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentDestination: NavDestination?,
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    onSignout: () -> Unit
){
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.primary
            ) {
                MotoConnectNavigation.values().forEach { item ->
                    BottomNavigationItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.name } == true,
                        selectedContentColor = MaterialTheme.colorScheme.secondary,
                        onClick = {
                            navController.navigate(item.name){
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = "Icon ${item.name}",
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
                                colorResource(id = R.color.black)
                            )
                        }
                    )
                }
            }
        }
    ) {
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MotoConnectNavigation.Homepage.name,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(MotoConnectNavigation.Homepage.name) {
                HomeScreen()
            }
            composable(MotoConnectNavigation.Map.name) {
                MapScreen()
            }
            composable(MotoConnectNavigation.Moto.name) {
                MotoScreen()
            }
            composable(MotoConnectNavigation.Profile.name) {
                ProfileScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = onSignout
                )
            }
        }
    }
}