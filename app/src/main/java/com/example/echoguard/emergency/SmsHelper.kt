package com.example.echoguard.emergency

import android.content.Context
import android.telephony.SmsManager
import android.util.Log

/**
 * SmsHelper: Handles the tactical broadcasting of SOS messages to emergency contacts.
 * Compatible with Android 8.0 through Android 16.
 */
class SmsHelper(private val context: Context) {

    /**
     * Sends the SOS message to the list of predefined contacts.
     * @param message The alert text including the Google Maps URL.
     */
    fun sendSmsToContacts(message: String) {
        // TODO: Future update - Fetch real contacts from DataStore or Room Database.
        // For now, these are placeholder test numbers.
        val contacts = listOf("911234567890", "919876543210")

        try {
            // Get the appropriate SmsManager instance for the current Android version
            val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

            for (contact in contacts) {
                // We use divideMessage to ensure long URLs/texts are handled as a single unit
                val parts = smsManager.divideMessage(message)

                if (parts.size > 1) {
                    smsManager.sendMultipartTextMessage(contact, null, parts, null, null)
                } else {
                    smsManager.sendTextMessage(contact, null, message, null, null)
                }

                Log.d("EchoGuard", "Tactical SOS SMS dispatched to: $contact")
            }
        } catch (e: Exception) {
            Log.e("EchoGuard", "SMS Broadcast Failure: ${e.message}")
            // Note: Failure here usually means airplane mode is on or there is no cellular signal.
        }
    }
}