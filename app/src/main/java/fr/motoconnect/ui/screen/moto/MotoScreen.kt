package fr.motoconnect.ui.screen.moto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.motoconnect.ui.screen.moto.components.CreateMotoComponent
import fr.motoconnect.ui.screen.moto.components.MotoDetailsComponent
import fr.motoconnect.viewmodel.AuthenticationViewModel
import fr.motoconnect.viewmodel.JourneyViewModel
import fr.motoconnect.viewmodel.MotoViewModel

@Composable
fun MotoScreen(
    authenticationViewModel: AuthenticationViewModel
) {
    val motoViewModel: MotoViewModel = viewModel()
    val journeyViewModel: JourneyViewModel = viewModel(factory = JourneyViewModel.Factory)
    val motoUIState = motoViewModel.motoUiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        item {
            if (motoUIState.value.moto == null) {
                CreateMotoComponent(
                    motoViewModel = motoViewModel,
                )
            } else {
                MotoDetailsComponent(motoViewModel = motoViewModel, journeyViewModel = journeyViewModel, authenticationViewModel = authenticationViewModel)
            }
        }
    }
}

