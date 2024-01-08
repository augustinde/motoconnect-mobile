package fr.motoconnect.ui.screen.journey.journeyList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.motoconnect.ui.navigation.MotoConnectNavigationRoutes
import fr.motoconnect.viewmodel.uiState.JourneyUIState

@Composable
fun JourneyListComponent(
    journeyUIState: JourneyUIState,
    navController: NavController,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(journeyUIState.journeys.size) { index ->
            JourneyCardComponent(journeyUIState.journeys[index], onClickSeeMore = {
                navController.popBackStack(MotoConnectNavigationRoutes.Journeys.name, true)
                navController.navigate(MotoConnectNavigationRoutes.JourneyDetails.name + "/" + journeyUIState.journeys[index].id){

                }
            })
        }
    }

}