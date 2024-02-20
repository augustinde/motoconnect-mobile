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
import fr.motoconnect.viewmodel.uiState.MapUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val TAG = "MapViewModel"

    private val _mapUiState = MutableStateFlow(MapUIState())
    val mapUiState: StateFlow<MapUIState> = _mapUiState.asStateFlow()

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    init {
        db.collection("users").document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    if(document.data?.get("device") != null){
                        getLiveData(document.data?.get("device") as String, document.data?.get("currentMoto") as String)
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

    private fun getLiveData(deviceId: String, currentMoto: String) {
        val dbRef = db.collection("devices")
            .document(deviceId)
        dbRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                _mapUiState.value = _mapUiState.value.copy(
                    currentMotoPosition = snapshot.data?.get("currentMotoPosition") as GeoPoint?,
                    deviceState = snapshot.data?.get("state") as Boolean?,
                    currentMoto = currentMoto
                )

            } else {
                Log.d(TAG, "Current data: null")
            }
        }
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
