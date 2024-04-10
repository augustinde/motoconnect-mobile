package fr.motoconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.model.JourneyPlayerState
import fr.motoconnect.data.model.PointObject
import fr.motoconnect.viewmodel.uiState.JourneyDetailsUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class JourneyDetailsViewModel : ViewModel() {

    val TAG = "JourneyDetailsViewModel"

    private val _journeyDetailsUiState = MutableStateFlow(JourneyDetailsUIState())
    val journeyDetailsUiState: StateFlow<JourneyDetailsUIState> =
        _journeyDetailsUiState.asStateFlow()

    val db = Firebase.firestore
    val auth = Firebase.auth

    suspend fun getJourney(journeyId: String, deviceId: String) {
        val dbref = db.collection("devices")
            .document(deviceId)
            .collection("journeys")
            .document(journeyId)

        try {
            val document = dbref.get().await()

            if (document != null) {
                val pointsList = coroutineScope {
                    async(Dispatchers.IO) {
                        db.collection("devices")
                            .document(deviceId)
                            .collection("journeys")
                            .document(journeyId)
                            .collection("points")
                            .orderBy("time")
                            .get().await()
                            .map { document ->
                                PointObject(
                                    geoPoint = document.get("geoPoint") as GeoPoint,
                                    speed = document.get("speed") as Long,
                                    time = document.get("time") as Timestamp,
                                    tilt = document.get("tilt") as Long,
                                )
                            }
                    }
                }.await()

                val journey = JourneyObject(
                    id = document.id,
                    startDateTime = document.get("startDateTime") as Timestamp?,
                    distance = document.get("distance") as Long?,
                    duration = document.get("duration") as Long?,
                    endDateTime = document.get("endDateTime") as Timestamp?,
                    maxSpeed = document.get("maxSpeed") as Long?,
                    points = pointsList,
                )
                _journeyDetailsUiState.value = JourneyDetailsUIState(
                    isLoading = false,
                    journey = journey,
                    errorMsg = null,
                    currentPoint = journey.points.first(),
                )
            }
        } catch (exception: Exception) {
            _journeyDetailsUiState.value = JourneyDetailsUIState(
                isLoading = false,
                journey = null,
                errorMsg = exception.message,
                currentPoint = null,
            )
            Log.w(TAG, "Error getting documents: ", exception)
        }
    }

    fun setCurrentPoint(point: PointObject) {
        _journeyDetailsUiState.value = _journeyDetailsUiState.value.copy(
            currentPoint = point,
        )
    }

    fun setPlayerState(state: JourneyPlayerState) {
        _journeyDetailsUiState.value = _journeyDetailsUiState.value.copy(playerState = state)
    }

    fun getPlayerState(): JourneyPlayerState {
        return _journeyDetailsUiState.value.playerState
    }
}
