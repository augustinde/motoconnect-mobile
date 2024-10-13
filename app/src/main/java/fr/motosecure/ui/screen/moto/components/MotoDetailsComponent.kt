package fr.motosecure.ui.screen.moto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import fr.motosecure.R
import fr.motosecure.viewmodel.JourneyViewModel
import fr.motosecure.viewmodel.MotoViewModel
import fr.motosecure.viewmodel.uiState.JourneyUIState
import fr.motosecure.viewmodel.uiState.MotoUIState

@Composable
fun MotoDetailsComponent(
    motoViewModel: MotoViewModel,
    motoUIState: MotoUIState,
    journeyUIState: JourneyUIState
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MotoFluidsComponent(
            motoUIState = motoUIState,
            journeyUIState = journeyUIState,
            resetEngineOil = {
                motoViewModel.resetEngineOil(journeyUIState.distanceTotal)
            },
            resetBrakeFluid = {
                motoViewModel.resetBrakeFluid(journeyUIState.distanceTotal)
            },
            resetChainLubrication = {
                motoViewModel.resetChainLubrication(journeyUIState.distanceTotal)
            }
        )
        MotoTyresComponent(
            motoUIState = motoUIState,
            journeyUIState = journeyUIState,
            resetFrontTyre = {
                motoViewModel.resetFrontTyre(journeyUIState.distanceTotal)
            },
            resetRearTyre = {
                motoViewModel.resetRearTyre(journeyUIState.distanceTotal)
            }
        )
        MotoJourneysComponent(journeyUIState = journeyUIState)
    }
}
