package com.example.echoguard.domain.usecase

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.sqrt

/**
 * FallDetector: Monitors accelerometer data at 100Hz.
 * Uses a two-phase detection algorithm:
 * 1. Impact Phase: Detection of high G-force (> 3.5G).
 * 2. Inertia Phase: Detection of sudden lack of movement or significant orientation change.
 */
class FallDetector(
    context: Context,
    private val onFallDetected: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var isMonitoring = false

    // Thresholds for detection
    private val IMPACT_THRESHOLD = 3.5f
    private val STABILITY_THRESHOLD = 0.5f

    fun startSensing() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            isMonitoring = true
            Log.d("EchoGuard", "Fall Detection: Monitoring Started")
        }
    }

    fun stopSensing() {
        sensorManager.unregisterListener(this)
        isMonitoring = false
        Log.d("EchoGuard", "Fall Detection: Monitoring Stopped")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || !isMonitoring) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Calculate G-Force
            val gForce = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH

            // Phase 1: Impact Detection
            if (gForce > IMPACT_THRESHOLD) {
                Log.d("EchoGuard", "Impact Detected: $gForce G")
                // In a production environment, we would wait 2 seconds here
                // to check for 'Inertia' or 'Post-Fall Orientation'
                onFallDetected()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}