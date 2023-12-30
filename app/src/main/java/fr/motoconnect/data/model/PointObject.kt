package fr.motoconnect.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class PointObject(
    val geoPoint: GeoPoint,
    val speed: Long,
    val time: Timestamp,
    val tilt: Long,
)