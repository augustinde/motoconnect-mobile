package fr.motoconnect.ui.screen.journey.journeyDetails.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.utils.MarkerCustomUtils
import fr.motoconnect.viewmodel.JourneyDetailsViewModel


@Composable
fun JourneyDetailsContent(
    journey: JourneyObject,
    journeyDetailsViewModel: JourneyDetailsViewModel
) {
    val context = LocalContext.current

    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = journeyDetailsUIState.journey?.points?.first()?.geoPoint?.let {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    it.latitude,
                    it.longitude
                ), 15f
            )
        }!!
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isTrafficEnabled = false,
            mapType = MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current,
                R.raw.map_style
            ),
        ),
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false,
        ),
    ) {
        DrawJourney(journey)

        if (journeyDetailsUIState.currentPoint != null) {
            Marker(
                state = MarkerState(
                    LatLng(
                        journeyDetailsUIState.currentPoint!!.geoPoint.latitude,
                        journeyDetailsUIState.currentPoint!!.geoPoint.longitude
                    )
                ),
                icon = MarkerCustomUtils().bitmapDescriptorFromVector(
                    context,
                    R.drawable.moto_position
                ),
                anchor = Offset(0.5f, 0.5f)
            )
        }
    }
    BoxOnMap(journeyDetailsViewModel = journeyDetailsViewModel)

}