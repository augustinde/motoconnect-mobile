package fr.motoconnect.data.utils

import android.content.Context
import android.util.Log
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import fr.motoconnect.R

class PermissionUtils {

    private val TAG = "PermissionUtils"

    @OptIn(ExperimentalPermissionsApi::class)
    fun askPermission(permission: PermissionState, context: Context, permissionName: String) {
        if (!permission.status.isGranted) {
            Log.d(TAG, context.getString(R.string.ask_permission, permissionName))
            permission.launchPermissionRequest()
        } else {
            Log.d(TAG, "Permission $permissionName granted")
        }
    }

}