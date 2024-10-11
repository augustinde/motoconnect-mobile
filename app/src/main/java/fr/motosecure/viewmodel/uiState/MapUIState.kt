package fr.motosecure.viewmodel.uiState

import com.google.firebase.firestore.GeoPoint

data class MapUIState(
    val currentMotoPosition: GeoPoint? = null,
    val currentMoto: String? = null,
)