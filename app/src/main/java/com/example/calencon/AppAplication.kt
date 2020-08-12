package com.example.calencon

import android.app.Application
import com.example.calencon.mechanics.appModules
import com.facebook.stetho.Stetho
import org.koin.core.context.startKoin

class AppApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupStetho()

    }
    private fun setupStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun setupKoin() {
        startKoin{
            modules(appModules)
        }
    }
}
