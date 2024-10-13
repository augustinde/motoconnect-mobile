package fr.motosecure.ui.screen.journey.journeyDetails

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.motosecure.R
import fr.motosecure.data.utils.TimeUtils
import fr.motosecure.ui.component.Loading
import fr.motosecure.ui.navigation.MotoConnectNavigationRoutes
import fr.motosecure.ui.screen.journey.journeyDetails.components.JourneyDetailsContent
import fr.motosecure.ui.screen.journey.journeyDetails.components.TravelNotFound
import fr.motosecure.viewmodel.JourneyDetailsViewModel

@Composable
fun JourneyDetailsScreen(
    journeyId: String?,
    navController: NavController,
) {

    val journeyDetailsViewModel: JourneyDetailsViewModel = viewModel()
    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    LaunchedEffect(journeyId) {
        journeyDetailsViewModel.getJourney(
            journeyId = journeyId!!,
        )
    }

    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.tertiary,
            title = {
                when (journeyDetailsUIState.journey) {
                    null -> {
                        Text(text = stringResource(R.string.journey_details))
                    }

                    else -> {
                        Text(
                            text = stringResource(
                                R.string.trajet_du, TimeUtils().toDateString(
                                    journeyDetailsUIState.journey?.startDateTime!!
                                )
                            ),
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(MotoConnectNavigationRoutes.Journeys.name)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            })
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {

            when {

                journeyDetailsUIState.isLoading -> {
                    Loading()
                }

                journeyDetailsUIState.errorMsg != null -> {
                    Text(
                        journeyDetailsUIState.errorMsg!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
                    )
                }

                journeyDetailsUIState.journey == null -> {
                    TravelNotFound()
                }

                else -> {
                    JourneyDetailsContent(journeyDetailsViewModel)
                }
            }
        }
    }
}




