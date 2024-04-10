package fr.motoconnect.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.R
import fr.motoconnect.architecture.MotoConnectApplication
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.model.PointObject
import fr.motoconnect.data.repository.GeocodingRepository
import fr.motoconnect.data.utils.JourneyUtils
import fr.motoconnect.viewmodel.uiState.JourneyUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class JourneyViewModel(
    private val geocodingRepository: GeocodingRepository,
    private val context: Context
) : ViewModel() {

    private val TAG = "JourneyViewModel"

    private val _journeyUiState = MutableStateFlow(JourneyUIState())
    val journeyUiState: StateFlow<JourneyUIState> = _journeyUiState.asStateFlow()

    val db = Firebase.firestore
    val auth = Firebase.auth

    fun getJourneys(deviceId: String?) {
        val dbRef = db.collection("devices")
            .document(deviceId ?: "null")
            .collection("journeys")

        dbRef.whereNotEqualTo("endDateTime", null).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.documents.isNotEmpty()) {
                val journeys = mutableListOf<JourneyObject>()

                for (document in snapshot.documents) {
                    if (document != null) {
                        val pointsRef = dbRef.document(document.id)
                            .collection("points")
                            .orderBy("time")

                        pointsRef.get().addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "DocumentSnapshot data: ${task.result}")
                                val duration = JourneyUtils().computeDurationInMinutes(
                                    task.result!!.first().get("time") as Timestamp,
                                    task.result!!.last().get("time") as Timestamp
                                )
                                Log.d(TAG, "duration: $duration")
                                val points = mutableListOf<PointObject>()
                                for (point in task.result!!) {
                                    points.add(
                                        PointObject(
                                            geoPoint = point.get("geoPoint") as GeoPoint,
                                            speed = point.get("speed") as Long,
                                            time = point.get("time") as Timestamp,
                                            tilt = point.get("tilt") as Long,
                                        )
                                    )
                                }
                                Log.d(TAG, "first point: ${points.first()}")
                                Log.d(TAG, "last point: ${points.last()}")

                                journeys.add(
                                    JourneyObject(
                                        id = document.id,
                                        startDateTime = document.get("startDateTime") as Timestamp?,
                                        distance = JourneyUtils().computeDistanceInKm(points),
                                        duration = duration,
                                        endDateTime = document.get("endDateTime") as Timestamp?,
                                        maxSpeed = JourneyUtils().computeMaxSpeed(points),
                                        points = points
                                    )
                                )
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.exception)
                            }
                        }
                    }
                }
                _journeyUiState.value =
                    JourneyUIState(journeys = journeys, isLoading = false, errorMsg = null)
            } else {
                Log.d(TAG, "Current data: null")
                _journeyUiState.value = JourneyUIState(
                    isLoading = false,
                    errorMsg = context.getString(R.string.journey_not_found),
                    journeys = emptyList()
                )
            }
        }
    }

    suspend fun reverseGeocoding(point: GeoPoint): String {
        return withContext(Dispatchers.IO) {
            geocodingRepository.getCity(point.latitude, point.longitude)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MotoConnectApplication)
                val geocodingRepository = application.container.geocodingRepository
                JourneyViewModel(geocodingRepository = geocodingRepository, context = application.applicationContext)
            }
        }
    }
}
