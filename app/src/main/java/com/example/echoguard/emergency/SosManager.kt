package com.example.echoguard.emergency

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * Handles the logic when a distress event is confirmed.
 * Orchestrates GPS fetching, Cloud Logging, and SMS Broadcasting.
 */
class SosManager(context: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val locationHelper = LocationHelper(context)
    private val smsHelper = SmsHelper(context)

    fun executeEmergencySequence(triggerType: String) {
        Log.d("EchoGuard", "INITIATING SOS SEQUENCE: $triggerType")

        val userId = auth.currentUser?.uid ?: "anonymous_unit"
        val incidentId = "EG-${System.currentTimeMillis()}"

        // 1. Fetch Location first as it is required for both SMS and Logs
        locationHelper.getCurrentLocation { location: Location ->
            val mapsUrl = "https://maps.google.com/?q=${location.latitude},${location.longitude}"

            // 2. Automated SMS Broadcast (Hardware Layer)
            val smsMessage = "ECHOGUARD ALERT: $triggerType. Location: $mapsUrl"
            smsHelper.sendSmsToContacts(smsMessage)

            // 3. Log to Firebase (Forensic Layer)
            val incident = hashMapOf(
                "incidentId" to incidentId,
                "trigger" to triggerType,
                "timestamp" to Date(),
                "location" to hashMapOf(
                    "lat" to location.latitude,
                    "lng" to location.longitude
                ),
                "status" to "EMERGENCY_ACTIVE",
                "maps_url" to mapsUrl
            )

            // Strict Firestore Path Rule applied here
            db.collection("artifacts").document("echoguard-sos")
                .collection("users").document(userId)
                .collection("incidents").document(incidentId)
                .set(incident)
                .addOnSuccessListener {
                    Log.d("EchoGuard", "Forensic Incident Logged Successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("EchoGuard", "Forensic Logging Failed: ${e.message}")
                }
        }
    }
}