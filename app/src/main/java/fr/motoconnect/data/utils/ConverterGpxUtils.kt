package fr.motoconnect.data.utils

import fr.motoconnect.data.model.JourneyObject

class ConverterGpxUtils {

    /**
     * Convert a journey to a gpx file
     * @param journey [JourneyObject]
     * @return String
     */
    fun convertGpxAndReturnString(journey: JourneyObject): String {
        val builder = StringBuilder()

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        builder.append("<gpx version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\">\n")
        builder.append("  <metadata>\n")
        builder.append("    <name>${TimeUtils().toDateTimeString(journey.startDateTime!!)}</name>\n")
        builder.append("  </metadata>\n")
        builder.append("  <trk>\n")
        builder.append("    <trkseg>\n")

        for (point in journey.points) {
            builder.append("      <trkpt lat=\"${point.geoPoint.latitude}\" lon=\"${point.geoPoint.longitude}\">\n")
            builder.append("      </trkpt>\n")
            builder.append("      <time>${point.time.toDate()}</time>\n")
        }

        builder.append("    </trkseg>\n")
        builder.append("  </trk>\n")
        builder.append("</gpx>\n")

        return builder.toString()
    }


    /**
     * Convert a journey to a kml file
     * @param journey [JourneyObject]
     * @return String
     */
    fun convertKmlAndReturnString(journey: JourneyObject): String{
        val builder = StringBuilder()

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        builder.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n")
        builder.append("  <Document>\n")
        builder.append("    <name>${TimeUtils().toDateTimeString(journey.startDateTime!!)}</name>\n")
        builder.append("    <description>Exported from MotoConnect</description>\n")
        builder.append("    <Style id=\"yellowLineGreenPoly\">\n")
        builder.append("      <LineStyle>\n")
        builder.append("        <color>7f00ffff</color>\n")
        builder.append("        <width>4</width>\n")
        builder.append("      </LineStyle>\n")
        builder.append("      <PolyStyle>\n")
        builder.append("        <color>7f00ff00</color>\n")
        builder.append("      </PolyStyle>\n")
        builder.append("    </Style>\n")
        builder.append("    <Placemark>\n")
        builder.append("      <name>Absolute Extruded</name>\n")
        builder.append("      <description>Transparent green wall with yellow outlines</description>\n")
        builder.append("      <styleUrl>#yellowLineGreenPoly</styleUrl>\n")
        builder.append("      <LineString>\n")
        builder.append("        <extrude>1</extrude>\n")
        builder.append("        <tessellate>1</tessellate>\n")
        builder.append("        <altitudeMode>absolute</altitudeMode>\n")
        builder.append("        <coordinates>\n")


        for (point in journey.points) {
            builder.append("          ${point.geoPoint.longitude},${point.geoPoint.latitude},0\n")
        }

        builder.append("        </coordinates>\n")
        builder.append("      </LineString>\n")
        builder.append("    </Placemark>\n")
        builder.append("  </Document>\n")
        builder.append("</kml>\n")

        return builder.toString()
    }
}