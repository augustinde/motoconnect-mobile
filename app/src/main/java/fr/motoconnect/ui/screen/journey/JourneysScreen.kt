package fr.motoconnect.ui.screen.journey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.ui.component.Loading
import fr.motoconnect.data.utils.TimestampUtils
import fr.motoconnect.viewmodel.JourneyViewModel

@Composable
fun JourneysScreen(
    navController: NavController,
) {

    val journeyViewModel: JourneyViewModel = viewModel()
    val journeyUIState = journeyViewModel.journeyUiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.journeys),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 24.sp,
            )
        }
        if (journeyUIState.value.errorMsg != null) {
            Text(
                journeyUIState.value.errorMsg!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
            )
        } else {
            if (journeyUIState.value.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Loading()
                }
            } else {
                JourneyListComponent(
                    journeys = journeyUIState.value.journeys,
                    navController = navController
                )
            }
        }
    }
}


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
                JourneyCard(journey, onClickSeeMore = {
                    navController.navigate("journeyDetails/" + journey.id)
                })
            }
        }
    }
}

@Composable
fun JourneyCard(
    journeyObject: JourneyObject,
    onClickSeeMore: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(0.dp, 0.dp, 16.dp, 0.dp),
        ) {
            Text(
                TimestampUtils().toDateTimeString(journeyObject.startDateTime!!),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
            )
            Text(
                stringResource(R.string.distance_km, journeyObject.distance!!),
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                stringResource(R.string.duration, journeyObject.duration?.toHourMinute()!!),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Button(
            modifier = Modifier
                .weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.tertiary,
            ),
            onClick = {
                onClickSeeMore()
            }
        ) {
            Text(stringResource(R.string.see_more))
        }
    }
}

fun Long.toHourMinute(): String {
    val hour = this / 60
    val minute = this % 60
    if (hour == 0L) return "${minute}min"
    return "${hour}h ${minute}min"
}
