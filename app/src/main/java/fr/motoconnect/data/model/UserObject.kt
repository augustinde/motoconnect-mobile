package fr.motoconnect.data.model

import com.google.firebase.firestore.GeoPoint

data class UserObject(
    val displayName: String? = null,
    val caseId: Int? = null,
    val caseState: Boolean? = false,
    val currentMoto: String? = null,
    val currentMotoPosition: GeoPoint? = null,
)