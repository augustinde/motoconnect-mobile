package fr.motoconnect.data.utils

import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import fr.motoconnect.data.model.DeviceObject
import kotlinx.coroutines.flow.MutableStateFlow


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

    fun startScan() {
        try {
            scanner.startScan()
                .addOnSuccessListener {
                    val splitBarcode = it.rawValue?.split(":")
                    if (splitBarcode!![0] == "motoConnect") {
                        pairDeviceAndUser(splitBarcode[1])
                    }
                }
        } catch (_: Exception) {
            Log.d(TAG, "Error during scan")
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
                            Log.d(
                                TAG,
                                "Device '$deviceId' paired with user '${auth.currentUser!!.uid}'"
                            )
                            resultPairing.value = true
                        }
                }

        } catch (_: Exception) {
            Log.d(TAG, "Error during pairing device '$deviceId' and user")
            resultPairing.value = false
        }

    }

}