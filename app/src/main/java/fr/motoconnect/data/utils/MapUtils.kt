package fr.motoconnect.data.utils

import com.google.android.gms.maps.model.LatLng

class MapUtils {

    fun calculateDistanceBetweenTwoPoints(
        currentDevicePosition: LatLng,
        currentMotoPosition: LatLng
    ): Long {
        val R = 6371

        val dLat = Math.toRadians(currentMotoPosition.latitude - currentDevicePosition.latitude)
        val dLon = Math.toRadians(currentMotoPosition.longitude - currentDevicePosition.longitude)

        val a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(currentDevicePosition.latitude)) * Math.cos(
                Math.toRadians(currentMotoPosition.latitude)
            ) * Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return Math.round(R * c)
    }
}