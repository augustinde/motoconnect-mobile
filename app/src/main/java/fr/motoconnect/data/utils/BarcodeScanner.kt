package fr.motoconnect.data.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import fr.motoconnect.R
import fr.motoconnect.data.model.DeviceObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking


class BarcodeScanner(
    appContext: Context,
) {

    private val TAG = "BarcodeScanner"
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    val resultPairing = MutableStateFlow<Boolean?>(false)
    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE
        )
        .build()
    private val scanner = GmsBarcodeScanning.getClient(appContext, options)
    private val context = appContext

    fun startScan() {
        try {
            scanner.startScan()
                .addOnSuccessListener {
                    val splitBarcode = it.rawValue?.split(":")
                    if (splitBarcode!![0] == "motoConnect") {
                        pairingProcess(splitBarcode[1])
                    }
                }
        } catch (_: Exception) {
            Log.d(TAG, "Error during scan")
        }
    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun pairingProcess(deviceId: String) = runBlocking {

        checkIfDeviceIsAlreadyPairedWithUser(deviceId){ isPaired ->
            if(!isPaired){
                Log.d(TAG, "Device is not paired with user")
                pairDeviceAndUser(deviceId)
            }else{
                Log.d(TAG, "Device is already paired with user")
                resultPairing.value = false
                showToast(context.getString(R.string.cannot_pair_device))
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
                            resultPairing.value = true
                            showToast(context.getString(R.string.device_paired))
                        }
                }

        } catch (_: Exception) {
            Log.d(TAG, "Error during pairing device and user")
            resultPairing.value = false
            showToast(context.getString(R.string.cannot_pair_device))
        }

    }

}