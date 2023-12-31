package fr.motoconnect.ui.screen.journey.journeyList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject

@Composable
fun JourneyListComponent(
    journeys: List<JourneyObject> = emptyList(),
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(0.dp, 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        if (journeys.isEmpty()) {
            Text(
                stringResource(R.string.no_journeys_yet),
            )
        } else {
            for (journey in journeys) {
                JourneyCardComponent(journey, onClickSeeMore = {
                    navController.navigate("journeyDetails/" + journey.id)
                })
            }
        }
    }
}