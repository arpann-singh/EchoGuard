package com.example.echoguard.emergency

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * LocationHelper: Fetches high-accuracy GPS coordinates for tactical alerts.
 * Uses Google Play Services Fused Location Provider for the best balance of speed and accuracy.
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Attempts to fetch the current high-accuracy location.
     * If fresh location fails, it attempts to fall back to the last known location.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {
        val cts = CancellationTokenSource()

        // Request a fresh, high-accuracy location fix
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cts.token
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d("EchoGuard", "Fresh GPS fix acquired: ${location.latitude}, ${location.longitude}")
                onLocationReceived(location)
            } else {
                Log.w("EchoGuard", "Fresh fix null, attempting last known location fallback...")
                fetchLastKnownLocation(onLocationReceived)
            }
        }.addOnFailureListener { e ->
            Log.e("EchoGuard", "Failed to get fresh location: ${e.message}")
            fetchLastKnownLocation(onLocationReceived)
        }
    }

    /**
     * Fallback mechanism to get the last location the device recorded.
     */
    @SuppressLint("MissingPermission")
    private fun fetchLastKnownLocation(onLocationReceived: (Location) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d("EchoGuard", "Fallback: Last known location acquired.")
                onLocationReceived(location)
            } else {
                Log.e("EchoGuard", "Critical Error: No location available (GPS/Network might be disabled)")
            }
        }
    }
}