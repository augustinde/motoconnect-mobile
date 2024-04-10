package fr.motoconnect.data.model

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.gson.Gson

data class JourneyObject(
    val id: String? = null,
    val startDateTime: Timestamp?,
    val distance: Long? = 0,
    val duration: Long? = 0,
    val endDateTime: Timestamp?,
    val maxSpeed: Long? = 0,
    val points: List<PointObject> = emptyList(),
){
    override fun toString(): String = Uri.encode(Gson().toJson(this))

}