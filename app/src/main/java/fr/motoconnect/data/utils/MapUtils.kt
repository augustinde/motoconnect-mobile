package fr.motoconnect.data.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.math.sqrt

class MapUtils {

    /**
     * Calculate the midpoint between two points
     * @param point1 [LatLng]
     * @param point2 [LatLng]
     * @return LatLng [LatLng]
     */
    fun calculateMidPoint(point1: LatLng, point2: LatLng): LatLng {
        // Convert latitude and longitude to radians
        val lat1 = Math.toRadians(point1.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon2 = Math.toRadians(point2.longitude)

        // Calculate components Bx and By for simplifying expressions
        val Bx = cos(lat2) * cos(lon2 - lon1)
        val By = cos(lat2) * sin(lon2 - lon1)

        // Calculate latitude of the midpoint (lat3)
        val lat3 = atan2(
            sin(lat1) + sin(lat2),
            sqrt((cos(lat1) + Bx) * (cos(lat1) + Bx) + By * By)
        )

        // Calculate longitude of the midpoint (lon3)
        val lon3 = lon1 + atan2(By, cos(lat1) + Bx)

        // Convert results back to degrees
        val latitude = Math.toDegrees(lat3)
        val longitude = Math.toDegrees(lon3)

        return LatLng(latitude, longitude)
    }

    /**
     * Calculate the zoom level between two points
     * @param point1 [LatLng]
     * @param point2 [LatLng]
     * @return LatLngBounds [LatLngBounds]
     */
    fun calculateZoomLevel(point1: LatLng, point2: LatLng): LatLngBounds {
        val builder = LatLngBounds.Builder()
        builder.include(point1)
        builder.include(point2)

        return builder.build()
    }

    /**
     * Calculate the distance between two points
     * @param currentDevicePosition [LatLng]
     * @param currentMotoPosition [LatLng]
     * @return Long [Long]
     */
    fun calculateDistanceBetweenTwoPoints(
        currentDevicePosition: LatLng,
        currentMotoPosition: LatLng
    ): Long {
        val R = 6371

        val dLat = Math.toRadians(currentMotoPosition.latitude - currentDevicePosition.latitude)
        val dLon = Math.toRadians(currentMotoPosition.longitude - currentDevicePosition.longitude)

        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(currentDevicePosition.latitude)) * cos(
                Math.toRadians(currentMotoPosition.latitude)
            ) * sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return (R * c).roundToLong()
    }
}