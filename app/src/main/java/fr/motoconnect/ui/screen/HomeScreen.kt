package fr.motoconnect.ui.screen

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
import fr.motoconnect.ui.navigation.MotoConnectNavigationRoutes
import fr.motoconnect.data.utils.MarkerCustomUtils
import fr.motoconnect.viewmodel.MapViewModel

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
    navController: NavController,
    mapViewModel: MapViewModel,
) {

    val TAG = "HomeScreen"
    val context = LocalContext.current

    val mapUiState = mapViewModel.mapUiState.collectAsState()

    val currentMotoPosition = mapUiState.value.currentMotoPosition?.let {
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
    LaunchedEffect(currentDevicePosition) {
        if (currentDevicePosition.latitude != 0.0 && currentDevicePosition.longitude != 0.0 && currentMotoPosition != null) {
            distanceBetweenDeviceAndMoto = mapViewModel.calculateDistanceBetweenTwoPoints(
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
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current,
                R.raw.map_style
            ),
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
                anchor = Offset(0.5f, 0.5f)
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

    if (mapUiState.value.weather != null) {
        WeatherInfo(
            temp = mapUiState.value.weather!!.temp.toString(),
            icon = "http:" + mapUiState.value.weather!!.icon
        )
    }

    if (mapUiState.value.currentMoto != null) {
        MapInfoMoto(
            navController = navController,
            distanceBetweenDeviceAndMoto = distanceBetweenDeviceAndMoto,
            currentMoto = mapUiState.value.currentMoto!!,
            caseState = mapUiState.value.caseState!!
        )
    }
}

@Composable
fun WeatherInfo(
    temp: String,
    icon: String,
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalContext.current)
            .data(data = icon)
            .build()
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .height(40.dp)
                .clip(RoundedCornerShape(20))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(8.dp),
        ) {
            Image(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp),
                painter = painter,
                contentDescription = "",
            )
            Text(text = "$temp°C")
        }
    }
}

@Composable
fun MapInfoMoto(
    navController: NavController,
    distanceBetweenDeviceAndMoto: String,
    currentMoto: String,
    caseState: Boolean
) {
    val caseStateString =
        if (caseState) stringResource(id = R.string.moto_in_motion) else stringResource(
            id = R.string.moto_stationary
        )
    val color = if (caseState) Color(0xFF52CA5E) else Color(0xFFEC6D50)

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .width(250.dp)
                .clip(RoundedCornerShape(20))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = currentMoto, fontWeight = FontWeight.Bold)
                Row {
                    Text(text = caseStateString)
                    Image(
                        painter = painterResource(id = R.drawable.moto_status),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color),
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(MotoConnectNavigationRoutes.Moto.name)
                        }
                )
                Text(text = stringResource(R.string.distance, distanceBetweenDeviceAndMoto))
            }
        }
    }
}

