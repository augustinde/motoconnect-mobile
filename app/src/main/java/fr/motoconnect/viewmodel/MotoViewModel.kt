package fr.motoconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.data.model.MotoObject
import fr.motoconnect.viewmodel.uiState.MotoUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MotoViewModel: ViewModel() {

    private val _motoUiState = MutableStateFlow(MotoUIState())
    val motoUiState: StateFlow<MotoUIState> = _motoUiState.asStateFlow()

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    init {
        getCurrentMoto()
    }

    fun getCurrentMoto(){
        try {
            val dbRef = db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("motos")
                .whereEqualTo("current", true)
            dbRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("MotoViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    Log.d("MotoViewModel", "Current data: ${snapshot.documents}")
                    val moto = snapshot.documents[0].toObject(MotoObject::class.java)
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

    fun createMoto(motoName: String) {
        val moto = MotoObject(name = motoName, current = true)
        try {
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("motos")
                .document(motoName)
                .set(moto)
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .update(
                    "currentMoto", motoName
                )

            _motoUiState.value = MotoUIState(moto = moto, errorMsg = null)
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

    fun resetEngineOil() {
        try {
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("motos")
                .document(motoUiState.value.moto?.name.toString())
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
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("motos")
                .document(motoUiState.value.moto?.name.toString())
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
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("motos")
                .document(motoUiState.value.moto?.name.toString())
                .update(
                    "chainLubrication", 0
                )
        } catch (e: Exception) {
            e.printStackTrace()
            _motoUiState.value = MotoUIState(errorMsg = e.message)
        }
    }

}
