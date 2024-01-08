package fr.motoconnect.ui.screen.journey.journeyList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.utils.MapUtils
import fr.motoconnect.data.utils.TimeUtils
import fr.motoconnect.ui.screen.journey.journeyDetails.components.DrawJourney
import fr.motoconnect.ui.store.DisplayStore
import fr.motoconnect.viewmodel.JourneyViewModel
import java.util.Locale

@Composable
fun JourneyCardComponent(
    journey: JourneyObject,
    onClickSeeMore: () -> Unit,
) {

    val journeyViewModel: JourneyViewModel = viewModel(factory = JourneyViewModel.Factory)
    val context = LocalContext.current

    val store = DisplayStore(context)
    val darkmode = store.getDarkMode.collectAsState(initial = false)

    var startCity by remember { mutableStateOf(context.resources.getString(R.string.unknown)) }
    var endCity by remember { mutableStateOf(context.resources.getString(R.string.unknown)) }

    val cameraPositionState = rememberCameraPositionState {
        position = journey.points.first().geoPoint.let {
            CameraPosition.fromLatLngZoom(
                MapUtils().calculateMidPoint(
                    LatLng(
                        journey.points.first().geoPoint.latitude,
                        journey.points.first().geoPoint.longitude
                    ),
                    LatLng(
                        journey.points.last().geoPoint.latitude,
                        journey.points.last().geoPoint.longitude
                    ),
                ), 13f
            )
        }
    }

    LaunchedEffect(journey) {

        startCity = journeyViewModel.reverseGeocoding(journey.points.first().geoPoint)
        endCity = journeyViewModel.reverseGeocoding(journey.points.last().geoPoint)

        cameraPositionState.move(
            CameraUpdateFactory.newLatLngBounds(
                MapUtils().calculateZoomLevel(
                    LatLng(
                        journey.points.first().geoPoint.latitude,
                        journey.points.first().geoPoint.longitude
                    ),
                    LatLng(
                        journey.points.last().geoPoint.latitude,
                        journey.points.last().geoPoint.longitude
                    ),
                ), 150
            )
        )
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClickSeeMore() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(150.dp)
        ) {
            GoogleMap(
                uiSettings = MapUiSettings(
                    compassEnabled = false,
                    myLocationButtonEnabled = false,
                    indoorLevelPickerEnabled = false,
                    mapToolbarEnabled = false,
                    rotationGesturesEnabled = false,
                    scrollGesturesEnabled = false,
                    scrollGesturesEnabledDuringRotateOrZoom = false,
                    tiltGesturesEnabled = false,
                    zoomControlsEnabled = false,
                    zoomGesturesEnabled = false,
                ),
                properties = MapProperties(
                    isTrafficEnabled = false,
                    mapType = MapType.NORMAL,
                    mapStyleOptions = if (!darkmode.value) {
                        MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style)
                    } else {
                        MapStyleOptions.loadRawResourceStyle(
                            LocalContext.current,
                            R.raw.map_style_dark
                        )
                    }
                ),
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                DrawJourney(journey = journey)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(150.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.onPrimary
                            ),
                            startY = 400f,
                            endY = 500f
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {

            Row(
                modifier = Modifier
            ) {
                Text(
                    text = TimeUtils().toDateDayString(journey.startDateTime!!)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Row(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                Column {
                    Text(
                        text = TimeUtils().toTime(journey.startDateTime?.seconds!!),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = TimeUtils().toTime(journey.endDateTime?.seconds!!),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.line_from_to),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .rotate(45f)
                            .width(20.dp)
                            .height(30.dp)
                    )
                }

                Column {
                    Text(
                        text = startCity.take(30),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = endCity.take(30),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.map),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .height(25.dp)
                            .width(25.dp)
                    )
                    Text(
                        text = "${journey.distance.toString()} km",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 10.sp,
                    )
                    Text(
                        text = stringResource(R.string.distance_without_args),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chrono),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .height(25.dp)
                            .width(25.dp)
                    )
                    Text(
                        text = TimeUtils().toHourMinute(journey.duration!!),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 10.sp,
                    )
                    Text(
                        text = stringResource(R.string.duration_without_args),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.speedometer),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .height(25.dp)
                            .width(25.dp)
                    )
                    Text(
                        text = "${journey.maxSpeed.toString()} km/h",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 10.sp,
                    )
                    Text(
                        text = stringResource(R.string.vitesse_max),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

        }
    }
}