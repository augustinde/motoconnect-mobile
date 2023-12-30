package fr.motoconnect.data.model

import com.google.firebase.Timestamp

data class JourneyObject(
    val id: String? = null,
    val startDateTime: Timestamp?,
    val distance: Long?,
    val duration: Long?,
    val endDateTime: Timestamp?,
    val averageSpeed: Long?,
    val finished: Boolean?,
    val points: List<PointObject> = emptyList(),
)