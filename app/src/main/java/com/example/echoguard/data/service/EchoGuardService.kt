package com.example.echoguard.data.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.echoguard.domain.usecase.AcousticDetector
import com.example.echoguard.domain.usecase.FallDetector
import com.example.echoguard.emergency.SosManager

/**
 * EchoGuardService: The primary background sensor orchestrator.
 * FIX: 'onBind' signature corrected for LifecycleService (non-nullable Intent).
 */
class EchoGuardService : LifecycleService() {

    private val CHANNEL_ID = "Guardian_Channel"
    private lateinit var acousticDetector: AcousticDetector
    private lateinit var fallDetector: FallDetector
    private lateinit var sosManager: SosManager

    override fun onCreate() {
        super.onCreate()
        sosManager = SosManager(this)

        // AI Detection Initializers
        acousticDetector = AcousticDetector(this) {
            sosManager.executeEmergencySequence("Acoustic AI Signature Detected")
        }

        fallDetector = FallDetector(this) {
            sosManager.executeEmergencySequence("Critical Impact Impact Detected")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("EchoGuard Protection: ACTIVE")
            .setContentText("Monitoring sensors for your safety.")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setOngoing(true)
            .build()

        startForeground(1, notification)

        acousticDetector.startSensing()
        fallDetector.startSensing()

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Safety Service", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        acousticDetector.stopSensing()
        fallDetector.stopSensing()
        super.onDestroy()
    }

    /**
     * Fixed signature to match LifecycleService.
     */
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}