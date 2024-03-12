package fr.motoconnect.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.R
import fr.motoconnect.architecture.MotoConnectApplication
import fr.motoconnect.data.model.DeviceObject
import fr.motoconnect.data.model.UserObject
import fr.motoconnect.viewmodel.uiState.DeviceUISate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DeviceViewModel(
    private val context: Context
) : ViewModel() {

    val db = Firebase.firestore
    val auth = Firebase.auth
    val TAG = "DeviceViewModel"

    private val _deviceUISate = MutableStateFlow(DeviceUISate())
    val deviceUiState: StateFlow<DeviceUISate> = _deviceUISate.asStateFlow()

    fun unpairDevice() {
        db.collection("devices")
            .document(deviceUiState.value.device!!.id!!)
            .update("user", null)
            .addOnSuccessListener {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .update("device", null)
                    .addOnSuccessListener {
                        showToast(context.getString(R.string.device_unpaired))
                        _deviceUISate.value = DeviceUISate(isUnpaired = true)
                    }
                    .addOnFailureListener {
                        showToast(context.getString(R.string.error_during_unpairing_device))
                        _deviceUISate.value = DeviceUISate(isUnpaired = false)
                    }
            }
            .addOnFailureListener {
                showToast(context.getString(R.string.error_during_unpairing_device))
                _deviceUISate.value = DeviceUISate(isUnpaired = false)
            }
    }

     fun getUserDevice() {
        try {
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { userDocument ->
                    val user = userDocument.toObject(UserObject::class.java)
                    if (user != null) {
                        if (user.device != null) {
                            db.collection("devices")
                                .document(user.device)
                                .get()
                                .addOnSuccessListener { deviceDocument ->
                                    val device = deviceDocument.toObject(DeviceObject::class.java)
                                    _deviceUISate.value = DeviceUISate(device = device)
                                }
                        }
                    }
                }
        } catch (_: Exception) {
            Log.d(TAG, "Error during getting user device")
        }
    }

    fun pairDevice(deviceId: String) {
        checkIfDeviceIsAlreadyPairedWithUser(deviceId) { isPaired ->
            if (!isPaired) {
                pairDeviceAndUser(deviceId)
            } else {
                showToast(context.getString(R.string.device_is_already_paired_with_user))
            }
        }
    }

    private fun checkIfDeviceIsAlreadyPairedWithUser(deviceId: String, callback: (Boolean) -> Unit) {
        try {
            db.collection("devices")
                .document(deviceId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val device = it.toObject(DeviceObject::class.java)
                        callback(device!!.user != null)
                    } else {
                        callback(false)
                    }
                }
        } catch (_: Exception) {
            Log.d(TAG, "Error during checking if device is already paired with user")
            callback(false)
        }
    }

    private fun pairDeviceAndUser(deviceId: String) {
        Log.d(TAG, deviceId + "test")
        try {
            val deviceObject = DeviceObject(auth.currentUser!!.uid, false, deviceId)
            db.collection("devices")
                .document(deviceId)
                .set(deviceObject)
                .addOnSuccessListener {
                    db.collection("users")
                        .document(auth.currentUser!!.uid)
                        .update(
                            mapOf(
                                "device" to deviceId
                            )
                        )
                        .addOnSuccessListener {
                            _deviceUISate.value = DeviceUISate(isPaired = true)
                            showToast(context.getString(R.string.device_paired))
                            getUserDevice()
                        }
                }

        } catch (_: Exception) {
            Log.d(TAG, "Error during pairing device and user")
            _deviceUISate.value = DeviceUISate(isPaired = false)
            showToast(context.getString(R.string.cannot_pair_device))
        }

    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MotoConnectApplication)
                DeviceViewModel(context = application.applicationContext)
            }
        }
    }

}
