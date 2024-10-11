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

@Composable
fun MotoDetailsComponent(
    motoViewModel: MotoViewModel,
    journeyViewModel: JourneyViewModel,
) {
    val motoUIState by motoViewModel.motoUiState.collectAsState()
    val journeyUIState by journeyViewModel.journeyUiState.collectAsState()

    LaunchedEffect(motoUIState.moto) {
        motoViewModel.getCurrentMoto()
        journeyViewModel.getJourneysCount()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MotoFluidsComponent(
            motoUIState = motoUIState,
            resetEngineOil = {
                motoViewModel.resetEngineOil()
            },
            resetBrakeFluid = {
                motoViewModel.resetBrakeFluid()
            },
            resetChainLubrication = {
                motoViewModel.resetChainLubrication()
            }
        )
        MotoTyresComponent(
            motoUIState = motoUIState,
            resetFrontTyre = {
                motoViewModel.resetFrontTyre()
            },
            resetRearTyre = {
                motoViewModel.resetRearTyre()
            }
        )
        MotoJourneysComponent(journeyUIState = journeyUIState)
    }
}
