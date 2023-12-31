package fr.motoconnect.ui.screen.moto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.viewmodel.MotoViewModel

@Composable
fun MotoDetailsComponent(
    motoViewModel: MotoViewModel,
) {

    val motoUIState by motoViewModel.motoUiState.collectAsState()
    LaunchedEffect(motoUIState.moto) {
        motoViewModel.getCurrentMoto()
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = motoUIState.moto?.name!!.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        MotoFluidsComponent(
            motoUIState = motoUIState,
            resetEngineOil = {
                motoViewModel.resetEngineOil()
            },
            resetBreakFluid = {
                motoViewModel.resetBreakFluid()
            },
            resetChainLubrication = {
                motoViewModel.resetChainLubrication()
            }
        )

        MotoJourneysComponent(motoUIState = motoUIState)

    }
}
