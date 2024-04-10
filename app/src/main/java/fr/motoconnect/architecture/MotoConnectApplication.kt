package fr.motoconnect.architecture

import android.app.Application
import fr.motoconnect.data.utils.NotificationService

class MotoConnectApplication: Application() {
    lateinit var container: AppContainer
    companion object {
        lateinit var instance: MotoConnectApplication
    }

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        instance = this

        val notificationService = NotificationService(this)
        notificationService.createNotificationChannel()

    }

}
