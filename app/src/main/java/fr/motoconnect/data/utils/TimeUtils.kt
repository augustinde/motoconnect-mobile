package fr.motoconnect.data.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class TimeUtils {

    fun toDateTimeString(timestamp: Timestamp): String {
        val date = Date(timestamp.seconds * 1000)
        val format = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return format.format(date)
    }

    fun toDateString(timeStamp: Timestamp): String {
        val date = Date(timeStamp.seconds * 1000L)
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        return sdf.format(date)
    }

    fun toTime(timeStamp: Long): String {
        val date = Date(timeStamp * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }

    fun toHourMinute(duration: Long): String {
        val hour = duration / 60
        val minute = duration % 60
        if (hour == 0L) return "${minute}min"
        return "${hour}h ${minute}min"
    }
}