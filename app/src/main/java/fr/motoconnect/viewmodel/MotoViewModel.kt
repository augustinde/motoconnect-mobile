package fr.motoconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.data.model.MotoObject
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

    private fun getCurrentMoto(){
        try {
            Log.d("TAG", "user : ${auth.currentUser?.uid.toString()}")
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("motos")
                .whereEqualTo("current", true)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val moto = document.toObject(MotoObject::class.java)
                        _motoUiState.value = MotoUIState(moto = moto, errorMsg = null)
                    }
                }
                .addOnFailureListener { exception ->
                    _motoUiState.value = MotoUIState(errorMsg = exception.message)
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
                .add(moto)
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

}

data class MotoUIState(
    val moto: MotoObject? = null,
    val errorMsg: String? = null,
)