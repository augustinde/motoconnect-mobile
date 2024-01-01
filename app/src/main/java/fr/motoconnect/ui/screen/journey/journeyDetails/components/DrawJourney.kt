package fr.motoconnect.ui.screen.journey.journeyDetails.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import fr.motoconnect.data.model.JourneyObject

@Composable
fun DrawJourney(
    journey: JourneyObject
) {
    for (i in 0 until journey.points.size - 1) {

        Polyline(
            points = listOf(
                LatLng(
                    journey.points[i].geoPoint.latitude,
                    journey.points[i].geoPoint.longitude
                ),
                LatLng(
                    journey.points[i + 1].geoPoint.latitude,
                    journey.points[i + 1].geoPoint.longitude
                )
            ),
            color = getColorBasedOnSpeed(journey.points[i].speed),
            width = 15f,
        )
    }

    Marker(
        state = MarkerState(
            LatLng(
                journey.points.first().geoPoint.latitude,
                journey.points.first().geoPoint.longitude
            )
        ),
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
    )
    Marker(
        state = MarkerState(
            LatLng(
                journey.points.last().geoPoint.latitude,
                journey.points.last().geoPoint.longitude
            )
        ),
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    )
}


private fun getColorBasedOnSpeed(speed: Long): Color {

    return when (speed) {
        in 0..20 -> Color(0xFF7dfa00)
        in 20..40 -> Color(0xFFa3f800)
        in 40..50 -> Color(0xFFdbf300)
        in 50..60 -> Color(0xFFf6d700)
        in 60..70 -> Color(0xFFffc900)
        in 70..80 -> Color(0xFFff8d00)
        in 80..90 -> Color(0xFFff6b00)
        else -> Color(0xFFfc4104)
    }

}
