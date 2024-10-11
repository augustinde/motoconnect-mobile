package fr.motosecure.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motosecure.R
import fr.motosecure.architecture.MotoSecureApplication
import fr.motosecure.data.model.JourneyObject
import fr.motosecure.data.model.PointObject
import fr.motosecure.data.repository.GeocodingRepository
import fr.motosecure.data.utils.JourneyUtils
import fr.motosecure.viewmodel.uiState.JourneyUIState
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

    fun getJourneys() {
        val dbRef = db.collection("motos")
            .document("Bandit 650")
            .collection("journeys")

        dbRef.whereNotEqualTo("endDateTime", null).get()
            .addOnSuccessListener { snapshot ->

            if (snapshot != null && snapshot.documents.isNotEmpty()) {
                val journeys = mutableListOf<JourneyObject>()

                for (document in snapshot.documents) {
                    if (document != null) {
                        Log.d(TAG, "Start date time: ${document.get("startDateTime")}")
                        val pointsRef = dbRef.document(document.id)
                            .collection("points")
                            .orderBy("time")
                            .limit(200)

                        getPointsInBatches(pointsRef, mutableListOf()) { points ->
                            Log.d(TAG, "first point: ${points.first()}")
                            Log.d(TAG, "last point: ${points.last()}")

                            journeys.add(
                                JourneyObject(
                                    id = document.id,
                                    startDateTime = document.get("startDateTime") as Timestamp?,
                                    distance = JourneyUtils().computeDistanceInKm(points),
                                    duration = JourneyUtils().computeDurationInMinutes(
                                        document.get("startDateTime") as Timestamp,
                                        document.get("endDateTime") as Timestamp
                                    ),
                                    endDateTime = document.get("endDateTime") as Timestamp?,
                                    maxSpeed = JourneyUtils().computeMaxSpeed(points),
                                    points = points
                                )
                            )
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

    fun getJourneysCount(){
        val dbRef = db.collection("motos")
            .document("Bandit 650")
            .collection("journeys")

        dbRef.whereNotEqualTo("endDateTime", null).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    _journeyUiState.value = JourneyUIState(
                        journeyCount = snapshot.documents.size,
                        isLoading = false,
                        errorMsg = null
                    )
                } else {
                    _journeyUiState.value = JourneyUIState(
                        isLoading = false,
                        errorMsg = context.getString(R.string.journey_not_found),
                        journeyCount = 0
                    )
                }
            }
    }

    private fun getPointsInBatches(pointsRef: Query, points: MutableList<PointObject>, onComplete: (List<PointObject>) -> Unit) {
        pointsRef.get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                points.add(
                    PointObject(
                        geoPoint = document.get("geoPoint") as GeoPoint,
                        speed = document.get("speed") as Long,
                        time = document.get("time") as Timestamp,
                        tilt = document.get("tilt") as Long,
                    )
                )
            }

            if (snapshot.size() == 200) { // If the maximum number of points was fetched, there might be more
                val lastVisible = snapshot.documents[snapshot.size() - 1]
                getPointsInBatches(pointsRef.startAfter(lastVisible), points, onComplete) // Fetch the next batch
            } else {
                onComplete(points) // All points were fetched, continue processing
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
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MotoSecureApplication)
                val geocodingRepository = application.container.geocodingRepository
                JourneyViewModel(geocodingRepository = geocodingRepository, context = application.applicationContext)
            }
        }
    }
}
