package fr.motoconnect.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import fr.motoconnect.MainActivity
import fr.motoconnect.R
import fr.motoconnect.ui.navigation.MotoConnectNavigationRoutes
import fr.motoconnect.viewmodel.MapViewModel

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
    navController: NavController
) {

    val TAG = "HomeScreen"
    val context = LocalContext.current

    val mapViewModel: MapViewModel = viewModel()
    val mapUiState = mapViewModel.mapUiState.collectAsState()

    val currentMotoPosition = mapUiState.value.currentPosition?.let {
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

    val locationResult = fusedLocationProviderClient.lastLocation
    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnownLocation = task.result
            currentDevicePosition =
                LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
        } else {
            Log.e(TAG, "Exception: ${task.exception}")
        }
    }

    LaunchedEffect(currentMotoPosition) {
        if (currentMotoPosition != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentMotoPosition, 16f)
        }
    }

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
            isIndoorEnabled = true,
            mapType = MapType.NORMAL,
        ),
    ) {

        if (currentMotoPosition != null) (
                Marker(
                    state = MarkerState(
                        position = currentMotoPosition
                    ),
                    icon = bitmapDescriptorFromVector(context, R.drawable.moto_position),
                )
                )

        if (currentDevicePosition.latitude != 0.0 && currentDevicePosition.longitude != 0.0) (
                Marker(
                    state = MarkerState(
                        position = currentDevicePosition
                    ),
                    icon = bitmapDescriptorFromVector(context, R.drawable.device_position),
                )
                )

    }
    if (mapUiState.value.currentMoto != null) {
        MapInfoMoto(
            navController = navController,
            distanceBetweenDeviceAndMoto = distanceBetweenDeviceAndMoto,
            currentMoto = mapUiState.value.currentMoto!!,
            caseState = mapUiState.value.caseState
        )
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


private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}