package fr.motoconnect.data.utils

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import fr.motoconnect.data.model.PointObject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class JourneyUtils {

    /**
     * Compute the max speed of a journey
     */
    fun computeMaxSpeed(points: List<PointObject>): Long {
        var maxSpeed = 0L
        for (point in points) {
            if (point.speed > maxSpeed) {
                maxSpeed = point.speed
            }
        }
        return maxSpeed
    }

    /**
     * Compute the duration of a journey in minutes
     */
    fun computeDurationInMinutes(startTimestamp: Timestamp, endTimestamp: Timestamp): Long {
        val startDateTimeMillis = startTimestamp.seconds * 1000
        val endDateTimeMillis = endTimestamp.seconds * 1000

        val durationInMillis = endDateTimeMillis - startDateTimeMillis
        val durationInMinutes = durationInMillis / 60000 // Conversion en minutes

        return if (durationInMillis % 60000 != 0L) {
            durationInMinutes + 1
        } else {
            durationInMinutes
        }
    }



    /**
     * Compute the distance of a journey in km
     */
    fun computeDistanceInKm(points: List<PointObject>): Long {
        var distance = 0.0
        for (i in 0 until points.size - 1) {
            distance += distanceBetweenPoints(points[i].geoPoint, points[i + 1].geoPoint)
        }
        return distance.toLong()
    }

    /**
     * Compute the distance between two points
     * @param point1 [GeoPoint]
     * @param point2 [GeoPoint]
     * @return Double
     */

    private fun distanceBetweenPoints(point1: GeoPoint, point2: GeoPoint): Double {
        val earthRadiusKm = 6371

        val lat1 = Math.toRadians(point1.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon2 = Math.toRadians(point2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }
}