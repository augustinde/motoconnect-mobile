package fr.motoconnect.data.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class TimeUtils {

    /**
     * Convert a timestamp to a date string "dd MMMM yyyy HH:mm"
     * @param timestamp [Timestamp]
     * @return date string
     */
    fun toDateTimeString(timestamp: Timestamp): String {
        val date = Date(timestamp.seconds * 1000)
        val format = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return format.format(date)
    }

    /**
     * Convert a timestamp to a date string "EEE dd MMMM yyyy"
     * @param timestamp [Timestamp]
     * @return date string
     */
    fun toDateDayString(timestamp: Timestamp): String {
        val date = Date(timestamp.seconds * 1000L)
        val sdf = SimpleDateFormat("EEEE dd MMMM yyyy")
        return sdf.format(date)
    }

    /**
     * Convert a timestamp to a date string "dd MMMM yyyy"
     * @param timestamp [Timestamp]
     * @return date string
     */
    fun toDateString(timestamp: Timestamp): String {
        val date = Date(timestamp.seconds * 1000L)
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        return sdf.format(date)
    }

    /**
     * Convert a timestamp to a date string "HH:mm"
     * @param timestamp [Timestamp]
     * @return date string
     */
    fun toTime(timestamp: Long): String {
        val date = Date(timestamp * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }

    /**
     * Convert a duration to hour and minute
     * @param duration [Long]
     * @return date string
     */
    fun toHourMinute(duration: Long): String {
        val hour = duration / 60
        val minute = duration % 60
        if (hour == 0L) return "$minute min"
        return "${hour}h $minute min"
    }
}