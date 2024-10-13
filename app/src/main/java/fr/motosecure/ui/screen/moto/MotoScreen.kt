package fr.motosecure.ui.screen.moto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.motosecure.ui.component.Loading
import fr.motosecure.ui.screen.moto.components.MotoDetailsComponent
import fr.motosecure.viewmodel.JourneyViewModel
import fr.motosecure.viewmodel.MotoViewModel

@Composable
fun MotoScreen() {
    val motoViewModel: MotoViewModel = viewModel()
    val journeyViewModel: JourneyViewModel = viewModel(factory = JourneyViewModel.Factory)
    val journeyUIState by journeyViewModel.journeyUiState.collectAsState()
    val motoUIState by motoViewModel.motoUiState.collectAsState()

    LaunchedEffect(true) {
        motoViewModel.getCurrentMoto()
        journeyViewModel.getJourneysDistanceTotal()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        when{
            journeyUIState.isLoading -> {
                Loading()
            }
            else -> {
                MotoDetailsComponent(
                    motoViewModel = motoViewModel,
                    motoUIState = motoUIState,
                    journeyUIState = journeyUIState)
            }
        }
    }
}

