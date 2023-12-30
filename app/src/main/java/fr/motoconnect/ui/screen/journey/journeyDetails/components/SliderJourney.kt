package fr.motoconnect.ui.screen.journey.journeyDetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.model.JourneyPlayerState
import fr.motoconnect.data.utils.TimestampUtils
import fr.motoconnect.viewmodel.JourneyDetailsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderJourney(
    journeyDetailsViewModel: JourneyDetailsViewModel
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = {
                togglePlayJourney(
                    journeyDetailsUIState.journey!!,
                    journeyDetailsViewModel,
                )
            },
            modifier = Modifier
                .background(Color(0xFF343333), RoundedCornerShape(50))
        ) {
            if (journeyDetailsUIState.playerState == JourneyPlayerState.PLAYING) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pause_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = TimestampUtils().toTime(journeyDetailsUIState.currentPoint?.time?.seconds!!),
            fontSize = 18.sp,
        )

        Slider(
            value = journeyDetailsUIState.currentPoint?.let {
                journeyDetailsUIState.journey?.points?.indexOf(
                    it
                )?.toFloat()!!
            } ?: 0f,
            onValueChange = {
                journeyDetailsViewModel.setCurrentPoint(
                    journeyDetailsUIState.journey?.points!![it.toInt()]
                )
                sliderPosition = it
            },
            valueRange = 0f..journeyDetailsUIState.journey?.points?.size?.toFloat()?.minus(1f)!!,
            steps = journeyDetailsUIState.journey?.points?.size?.minus(2)!!,
            thumb = {
                Image(
                    painter = painterResource(id = R.drawable.moto_position),
                    contentDescription = null,
                    Modifier
                        .height(50.dp)
                        .width(50.dp)
                )
            },
            enabled = journeyDetailsUIState.playerState != JourneyPlayerState.PLAYING,
        )

    }
}

private fun togglePlayJourney(
    journey: JourneyObject,
    journeyDetailsViewModel: JourneyDetailsViewModel,
) {
    when (journeyDetailsViewModel.journeyDetailsUiState.value.playerState) {
        JourneyPlayerState.PLAYING -> {
            journeyDetailsViewModel.setPlayerState(JourneyPlayerState.PAUSED)
        }

        JourneyPlayerState.STOPPED -> {
            journeyDetailsViewModel.setPlayerState(JourneyPlayerState.PLAYING)
            playJourney(journey, journeyDetailsViewModel)
        }

        else -> {
            journeyDetailsViewModel.setPlayerState(JourneyPlayerState.PLAYING)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun playJourney(journey: JourneyObject, journeyDetailsViewModel: JourneyDetailsViewModel) {

    GlobalScope.launch {
        for (i in 0 until journey.points.size) {
            if (journeyDetailsViewModel.getPlayerState() == JourneyPlayerState.PAUSED) {
                while (journeyDetailsViewModel.getPlayerState() == JourneyPlayerState.PAUSED) {
                    kotlinx.coroutines.delay(100)
                }
            }

            journeyDetailsViewModel.setCurrentPoint(journey.points[i])
            kotlinx.coroutines.delay(1000)
        }

        journeyDetailsViewModel.setCurrentPoint(journey.points.first())
        journeyDetailsViewModel.setPlayerState(JourneyPlayerState.STOPPED)
    }
}