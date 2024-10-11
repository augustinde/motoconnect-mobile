package fr.motosecure.ui.screen.journey.journeyDetails.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
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
import fr.motosecure.R
import fr.motosecure.data.utils.MapUtils
import fr.motosecure.data.utils.MarkerCustomUtils
import fr.motosecure.ui.store.DisplayStore
import fr.motosecure.viewmodel.JourneyDetailsViewModel

@Composable
fun JourneyDetailsContent(
    journeyDetailsViewModel: JourneyDetailsViewModel
) {
    val context = LocalContext.current

    val store = DisplayStore(context)
    val darkmode = store.getDarkMode.collectAsState(initial = false)

    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = journeyDetailsUIState.journey?.points?.first()?.geoPoint?.let {
            CameraPosition.fromLatLngZoom(
                MapUtils().calculateMidPoint(
                    LatLng(
                        journeyDetailsUIState.journey?.points?.first()?.geoPoint!!.latitude,
                        journeyDetailsUIState.journey?.points?.first()?.geoPoint!!.longitude
                    ),
                    LatLng(
                        journeyDetailsUIState.journey?.points?.last()?.geoPoint!!.latitude,
                        journeyDetailsUIState.journey?.points?.last()?.geoPoint!!.longitude
                    ),
                ), 15f
            )
        }!!
    }

    LaunchedEffect(cameraPositionState) {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngBounds(
                MapUtils().calculateZoomLevel(
                    LatLng(
                        journeyDetailsUIState.journey?.points?.first()?.geoPoint!!.latitude,
                        journeyDetailsUIState.journey?.points?.first()?.geoPoint!!.longitude
                    ),
                    LatLng(
                        journeyDetailsUIState.journey?.points?.last()?.geoPoint!!.latitude,
                        journeyDetailsUIState.journey?.points?.last()?.geoPoint!!.longitude
                    ),
                ), 300
            )
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isTrafficEnabled = false,
            mapType = MapType.NORMAL,
            mapStyleOptions = if (!darkmode.value) {
                MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style)
            } else {
                MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style_dark)
            }
        ),
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false,
        ),
    ) {
        DrawJourney(journeyDetailsUIState.journey!!)

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