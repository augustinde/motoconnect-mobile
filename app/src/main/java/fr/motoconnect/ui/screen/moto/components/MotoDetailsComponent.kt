package fr.motoconnect.ui.screen.moto.components

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
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = motoUIState.moto?.name!!.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                modifier = Modifier.padding(0.dp, 20.dp)
            )
        }

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
