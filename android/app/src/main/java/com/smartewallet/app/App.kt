package com.smartewallet.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.smartewallet.app.di.ServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize ServiceLocator (Dependency Injection)
        ServiceLocator.init(this)
    }
}