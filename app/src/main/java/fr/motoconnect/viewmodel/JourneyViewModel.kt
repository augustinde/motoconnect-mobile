package fr.motoconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.data.model.JourneyObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class JourneyViewModel: ViewModel() {

    private val TAG = "JourneyViewModel"

    private val _journeyUiState = MutableStateFlow(JourneyUIState())
    val journeyUiState: StateFlow<JourneyUIState> = _journeyUiState.asStateFlow()

    val db = Firebase.firestore
    val auth = Firebase.auth

    init {
        viewModelScope.launch {
            getJourneys()
            Log.d(TAG, journeyUiState.value.journeys.toString())
        }
    }

    private fun getJourneys() {
        _journeyUiState.value = JourneyUIState(isLoading = true)
        val dbRef = db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("journeys")
        dbRef.addSnapshotListener() { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.documents.isNotEmpty()) {
                Log.d(TAG, "Current data: ${snapshot.documents}")
                val journeys = mutableListOf<JourneyObject>()
                for (document in snapshot.documents) {
                    Log.d(TAG, "getJourneys: " + document.data)

                    if (document != null) {
                        journeys.add(
                            JourneyObject(
                                startDateTime = document.get("startDateTime") as Timestamp?,
                                distance = document.get("distance") as Long?,
                                duration = document.get("duration") as Long?,
                                endDateTime = document.get("endDateTime") as Timestamp?,
                                averageSpeed = document.get("averageSpeed") as Long?,
                                finished = document.get("finished") as Boolean?,
                            )
                        )
                    }
                }
                _journeyUiState.value = JourneyUIState(journeys = journeys, isLoading = false)
            } else {
                Log.d(TAG, "Current data: null")
                _journeyUiState.value = JourneyUIState(isLoading = false)
            }
        }


    }

}


data class JourneyUIState(
    val journeys: List<JourneyObject> = emptyList(),
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
)