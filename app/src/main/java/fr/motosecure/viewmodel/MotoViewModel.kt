package fr.motosecure.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motosecure.data.model.MotoObject
import fr.motosecure.viewmodel.uiState.MotoUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MotoViewModel: ViewModel() {

    private val _motoUiState = MutableStateFlow(MotoUIState())
    val motoUiState: StateFlow<MotoUIState> = _motoUiState.asStateFlow()

    private val db = Firebase.firestore

    init {
        getCurrentMoto()
    }

    fun getCurrentMoto(){
        try {
            val dbRef = db.collection("motos")
                .whereEqualTo("current", true) //todo: change logic
            dbRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("MotoViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    Log.d("MotoViewModel", "Current data: ${snapshot.documents}")
                    val moto = snapshot.documents[0].toObject(MotoObject::class.java)
                    moto?.id = snapshot.documents[0].id
                    _motoUiState.value = MotoUIState(moto = moto, errorMsg = null)
                } else {
                    Log.d("MotoViewModel", "Current data: null")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

    fun resetEngineOil() {
        try {
            db.collection("motos")
                .document("Bandit 650")
                .update(
                    "engineOil", 0
                )
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

    fun resetBrakeFluid() {
        try {
            db.collection("motos")
                .document("Bandit 650")
                .update(
                    "brakeFluid", 0
                )
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

    fun resetChainLubrication() {
        try {
            db.collection("motos")
                .document("Bandit 650")
                .update(
                    "chainLubrication", 0
                )
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

    fun resetFrontTyre() {
        try {
            db.collection("motos")
                .document("Bandit 650")
                .update(
                    "frontTyreWear", 0
                )
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

    fun resetRearTyre() {
        try {
            db.collection("motos")
                .document("Bandit 650")
                .update(
                    "rearTyreWear", 0
                )
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

}
