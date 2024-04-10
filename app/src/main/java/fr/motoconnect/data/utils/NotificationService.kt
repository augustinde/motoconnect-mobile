package fr.motoconnect.data.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import fr.motoconnect.MainActivity
import fr.motoconnect.R

class NotificationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            "notificationID",
            "motoConnectChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun sendNotification(title: String, message: String, notificationId: Int, onNotificationSent: (Int) -> Unit = {}) {

        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "https://motoconnect.com/moto".toUri(),
            context,
            MainActivity::class.java
        ).apply {
            putExtra("notificationId", notificationId) // Include notification ID
            putExtra("isNotificationClicked", true)
        }

        val pending: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, "notificationID")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pending)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
        onNotificationSent(notificationId)
    }

    fun getActiveNotification(notificationId: Int) {
        val activeNotifications = notificationManager.activeNotifications

        for (notification in activeNotifications) {
            val activeNotificationId = notification.id
            if (activeNotificationId == notificationId) {
                notificationManager.cancel(notificationId)
                Log.d("NOTIF", "Notification cancelled: $notificationId")
                break // Exit loop after finding a match
            }
        }
    }
}