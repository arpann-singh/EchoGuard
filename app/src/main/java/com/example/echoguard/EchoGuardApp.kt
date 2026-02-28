package com.example.echoguard

import android.app.Application
import com.google.firebase.FirebaseApp

/**
 * EchoGuardApp: Entry point for the application.
 * FIX: Removed accidental Service code from this file.
 */
class EchoGuardApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase for incident logs.
        FirebaseApp.initializeApp(this)
    }
}