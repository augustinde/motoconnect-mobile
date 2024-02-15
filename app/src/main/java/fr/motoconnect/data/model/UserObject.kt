package fr.motoconnect.data.model

import com.google.firebase.firestore.GeoPoint

data class UserObject(
    val displayName: String? = null,
    val currentMoto: String? = null,
    val currentMotoPosition: GeoPoint? = null,
    val device: String? = null,
)