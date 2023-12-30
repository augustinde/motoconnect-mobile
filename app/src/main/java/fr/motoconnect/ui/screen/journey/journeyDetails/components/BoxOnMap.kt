package fr.motoconnect.ui.screen.journey.journeyDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.motoconnect.viewmodel.JourneyDetailsViewModel

@Composable
fun BoxOnMap(
    journeyDetailsViewModel: JourneyDetailsViewModel
) {
    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PointInfo(point = journeyDetailsUIState.currentPoint!!)
            SliderJourney(
                journeyDetailsViewModel = journeyDetailsViewModel
            )
        }

    }
}