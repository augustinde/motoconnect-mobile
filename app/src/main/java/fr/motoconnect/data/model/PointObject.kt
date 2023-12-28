package fr.motoconnect.data.model

data class PointObject(
    val latitude: Double,
    val longitude: Double,
    val speed: Double,
    val time: Long,
    val tilt: Double,
)