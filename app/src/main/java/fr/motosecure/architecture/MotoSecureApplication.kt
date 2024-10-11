package fr.motosecure.architecture

import android.app.Application

class MotoSecureApplication: Application() {
    lateinit var container: AppContainer
    companion object {
        lateinit var instance: MotoSecureApplication
    }

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        instance = this
    }

}
