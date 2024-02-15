package fr.motoconnect.ui.screen.journey.journeyList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.motoconnect.R
import fr.motoconnect.ui.component.Loading
import fr.motoconnect.ui.screen.journey.journeyList.components.JourneyListComponent
import fr.motoconnect.viewmodel.AuthenticationViewModel
import fr.motoconnect.viewmodel.JourneyViewModel

@Composable
fun JourneyListScreen(
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel
) {

    val journeyViewModel: JourneyViewModel = viewModel(factory = JourneyViewModel.Factory)
    val journeyUIState by journeyViewModel.journeyUiState.collectAsState()
    val authUIState by authenticationViewModel.authUiState.collectAsState()

    LaunchedEffect(true) {
        journeyViewModel.getJourneys(deviceId = authUIState.device?.id!!)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.journeys),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                modifier = Modifier.padding(0.dp, 20.dp)
            )
        }

        when {

            journeyUIState.isLoading -> {
                Loading()
            }

            journeyUIState.errorMsg != null -> {
                Text(
                    journeyUIState.errorMsg!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
                )
            }

            journeyUIState.journeys.isEmpty() -> {
                Loading()
            }

            else -> {
                JourneyListComponent(
                    journeyUIState = journeyUIState,
                    navController = navController
                )
            }
        }
    }
}
