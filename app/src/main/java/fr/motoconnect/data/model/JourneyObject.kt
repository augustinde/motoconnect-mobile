package fr.motoconnect.data.model

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.gson.Gson

data class JourneyObject(
    val id: String? = null,
    val startDateTime: Timestamp?,
    val distance: Long?,
    val duration: Long?,
    val endDateTime: Timestamp?,
    val maxSpeed: Long?,
    val finished: Boolean?,
    val points: List<PointObject> = emptyList(),
){
    override fun toString(): String = Uri.encode(Gson().toJson(this))

}