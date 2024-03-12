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
import fr.motoconnect.viewmodel.MotoViewModel

@Composable
fun MotoScreen(
) {
    val motoViewModel: MotoViewModel = viewModel()
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
                MotoDetailsComponent(motoViewModel = motoViewModel)
            }
        }
    }
}

