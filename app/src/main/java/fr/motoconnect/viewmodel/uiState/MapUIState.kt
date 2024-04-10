package fr.motoconnect.viewmodel.uiState

import com.google.firebase.firestore.GeoPoint
import fr.motoconnect.data.model.WeatherObject

data class MapUIState(
    val currentMotoPosition: GeoPoint? = null,
    val currentMoto: String? = null,
    val weather: WeatherObject? = null
)