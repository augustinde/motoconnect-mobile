package fr.motoconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.architecture.MotoConnectApplication
import fr.motoconnect.data.model.WeatherObject
import fr.motoconnect.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.round
import java.lang.Math.sin
import java.lang.Math.sqrt

class MapViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val TAG = "MapViewModel"

    private val _mapUiState = MutableStateFlow(MapUIState())
    val mapUiState: StateFlow<MapUIState> = _mapUiState.asStateFlow()

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    init {
        getLiveData()
    }

    private fun getLiveData() {
        val dbRef = db.collection("users")
            .document(auth.currentUser?.uid.toString())
        dbRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                _mapUiState.value =
                    MapUIState(
                        currentMotoPosition = snapshot.data?.get("currentMotoPosition") as GeoPoint?,
                        currentMoto = snapshot.data?.get("currentMoto") as String?,
                        caseState = snapshot.data?.get("caseState") as Boolean?
                    )
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun calculateDistanceBetweenTwoPoints(
        currentDevicePosition: LatLng,
        currentMotoPosition: LatLng
    ): Long {
        val R = 6371

        val dLat = Math.toRadians(currentMotoPosition.latitude - currentDevicePosition.latitude)
        val dLon = Math.toRadians(currentMotoPosition.longitude - currentDevicePosition.longitude)

        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(currentDevicePosition.latitude)) * cos(
                Math.toRadians(currentMotoPosition.latitude)
            ) * sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return round(R * c)
    }

    fun getWeather(currentDevicePosition: LatLng) {
        viewModelScope.launch {
            val weather = weatherRepository.getCurrentWeather(
                currentDevicePosition.latitude,
                currentDevicePosition.longitude
            )
            _mapUiState.value = _mapUiState.value.copy(
                weather = WeatherObject(
                    temp = weather.current.tempC,
                    condition = weather.current.condition.text,
                    icon = weather.current.condition.icon
                )
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MotoConnectApplication)
                val weatherRepository = application.container.weatherRepository
                MapViewModel(weatherRepository = weatherRepository)
            }
        }
    }
}

data class MapUIState(
    val currentMotoPosition: GeoPoint? = null,
    val currentMoto: String? = null,
    val caseState: Boolean? = false,
    val weather: WeatherObject? = null
)