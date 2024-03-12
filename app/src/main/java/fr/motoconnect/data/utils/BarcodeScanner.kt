package fr.motoconnect.data.utils

import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import fr.motoconnect.viewmodel.DeviceViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class BarcodeScanner(
    appContext: Context,
    private val deviceViewModel: DeviceViewModel
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
                        deviceViewModel.pairDevice(splitBarcode[1])
                    }
                }
        } catch (_: Exception) {
            Log.d(TAG, "Error during scan")
        }
    }

}