package fr.motoconnect.architecture

import android.app.Application

class MotoConnectApplication: Application() {
    lateinit var container: AppContainer
    companion object {
        lateinit var instance: MotoConnectApplication
    }

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        instance = this
    }
}
