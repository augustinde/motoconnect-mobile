package fr.motosecure.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motosecure.viewmodel.uiState.MapUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel : ViewModel() {

    private val TAG = "MapViewModel"

    private val _mapUiState = MutableStateFlow(MapUIState())
    val mapUiState: StateFlow<MapUIState> = _mapUiState.asStateFlow()

    private val db = Firebase.firestore

    init {
        getLiveData()
    }

    private fun getLiveData() {
        val dbRef = db.collection("motos")
            .document("Bandit 650")
        dbRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                _mapUiState.value = _mapUiState.value.copy(
                    currentMotoPosition = snapshot.data?.get("currentMotoPosition") as GeoPoint?,
                    currentMoto = "Bandit 650"
                )

            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }
}
