package fr.motosecure.data.utils

import android.content.Context
import android.util.Log
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import fr.motosecure.R

class PermissionUtils {

    private val TAG = "PermissionUtils"

    /**
     * Ask permission to user
     * @param permission PermissionState [PermissionState]
     * @param context Context [Context]
     * @param permissionName String
     */
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