package com.example.echoguard.data.service

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.util.Log
import com.example.echoguard.emergency.SosManager

/**
 * EchoAccessibilityService: Detects rapid power button presses.
 * Triggers SOS even if the screen is dark or the phone is locked.
 */
class EchoAccessibilityService : AccessibilityService() {

    private var lastClickTime: Long = 0
    private var clickCount = 0
    private val CLICK_THRESHOLD = 2000 // 2 seconds window
    private val REQUIRED_CLICKS = 5

    private lateinit var sosManager: SosManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        sosManager = SosManager(this)
        Log.d("EchoGuard", "Power SOS Service Connected")
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val keyCode = event.keyCode

        // Intercept Power Button (Note: Some devices may require Volume keys instead
        // if the OEM blocks Power Button interception)
        if (keyCode == KeyEvent.KEYCODE_POWER || keyCode == KeyEvent.KEYCODE_SOFT_SLEEP) {
            if (event.action == KeyEvent.ACTION_DOWN) {
                handlePowerClick()
            }
            // Return true if you want to consume the event,
            // but for Power Button we usually let it pass so the screen still toggles
            return false
        }
        return super.onKeyEvent(event)
    }

    private fun handlePowerClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime < CLICK_THRESHOLD) {
            clickCount++
        } else {
            clickCount = 1
        }

        lastClickTime = currentTime
        Log.d("EchoGuard", "Power Click Count: $clickCount")

        if (clickCount >= REQUIRED_CLICKS) {
            Log.d("EchoGuard", "CRITICAL: Power Button SOS Triggered")
            sosManager.executeEmergencySequence("Power Button Rapid Press")
            clickCount = 0 // Reset after trigger
        }
    }

    override fun onInterrupt() {}
    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent) {}
}