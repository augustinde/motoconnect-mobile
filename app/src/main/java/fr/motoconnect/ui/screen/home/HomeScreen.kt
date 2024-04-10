package fr.motoconnect.ui.screen.home

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
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
import fr.motoconnect.MainActivity
import fr.motoconnect.R
import fr.motoconnect.data.utils.MapUtils
import fr.motoconnect.data.utils.MarkerCustomUtils
import fr.motoconnect.ui.screen.home.components.MapInfoMoto
import fr.motoconnect.ui.screen.home.components.WeatherInfo
import fr.motoconnect.ui.store.DisplayStore
import fr.motoconnect.viewmodel.MapViewModel

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
    mapViewModel: MapViewModel,
) {

    val TAG = "HomeScreen"
    val context = LocalContext.current

    val store = DisplayStore(context)
    val darkmode = store.getDarkMode.collectAsState(initial = false)

    val mapUiState by mapViewModel.mapUiState.collectAsState()

    val currentMotoPosition = mapUiState.currentMotoPosition?.let {
        LatLng(it.latitude, it.longitude)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(48.866667, 2.333333), 16f)
    }

    val fusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    var lastKnownLocation by remember {
        mutableStateOf<Location?>(null)
    }

    var currentDevicePosition by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var distanceBetweenDeviceAndMoto by remember {
        mutableStateOf(
            context.applicationContext.getText(
                R.string.unknown
            ).toString()
        )
    }

    //Get device position and get weather
    val locationResult = fusedLocationProviderClient.lastLocation
    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnownLocation = task.result
            currentDevicePosition =
                LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
            if (currentDevicePosition.latitude != 0.0 && currentDevicePosition.longitude != 0.0) {
                mapViewModel.getWeather(currentDevicePosition)
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(currentDevicePosition, 16f)
            }
        } else {
            Log.e(TAG, "Exception: ${task.exception}")
        }
    }

    //Set camera position to moto position
    LaunchedEffect(currentMotoPosition) {
        if (currentMotoPosition != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentMotoPosition, 16f)
        }
    }

    //Calculate distance between device and moto
    LaunchedEffect(currentDevicePosition, currentMotoPosition) {
        if (currentDevicePosition.latitude != 0.0 && currentDevicePosition.longitude != 0.0 && currentMotoPosition != null) {
            distanceBetweenDeviceAndMoto = MapUtils().calculateDistanceBetweenTwoPoints(
                currentDevicePosition,
                currentMotoPosition
            ).toString() + " km"
            Log.d(
                TAG,
                "Distance : $distanceBetweenDeviceAndMoto between $currentDevicePosition and $currentMotoPosition"
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isTrafficEnabled = true,
            mapType = MapType.NORMAL,
            mapStyleOptions = if (!darkmode.value) {
                MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style)
            } else {
                MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style_dark)
            }
        ),
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
        )
    ) {
        if (currentMotoPosition != null) {
            Marker(
                state = MarkerState(
                    position = currentMotoPosition
                ),
                icon = MarkerCustomUtils().bitmapDescriptorFromVector(
                    context,
                    R.drawable.moto_position
                ),
                anchor = Offset(0.5f, 0.5f),
                title = mapUiState.currentMoto,
            )
        }

        if (currentDevicePosition.latitude != 0.0 && currentDevicePosition.longitude != 0.0) {
            Marker(
                state = MarkerState(
                    position = currentDevicePosition
                ),
                icon = MarkerCustomUtils().bitmapDescriptorFromVector(
                    context,
                    R.drawable.device_position
                ),
                anchor = Offset(0.5f, 0.5f)
            )
        }

    }

    if (mapUiState.weather != null) {
        WeatherInfo(
            temp = mapUiState.weather!!.temp.toString(),
            icon = "http:" + mapUiState.weather!!.icon
        )
    }

    if (mapUiState.currentMoto != null) {
        MapInfoMoto(
            distanceBetweenDeviceAndMoto = distanceBetweenDeviceAndMoto,
            currentMoto = mapUiState.currentMoto!!
        )
    }
}


