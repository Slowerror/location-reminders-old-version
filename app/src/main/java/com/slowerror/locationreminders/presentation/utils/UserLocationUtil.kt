package com.slowerror.locationreminders.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber

class UserLocationUtil(context: Context, externalScope: CoroutineScope) {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    private val _locationUpdates = callbackFlow {

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Timber.i("onLocationResult() is called")
                result.locations.lastOrNull()?.let { location ->
                    trySend(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            Timber.i("addOnFailureListener is called")
            close(e)
        }

        awaitClose {
            Timber.i("awaitClose is called")
            fusedLocationClient.removeLocationUpdates(callback)
        }

    }.shareIn(
        externalScope,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )


    fun getLocationUpdates(): Flow<Location> {
        Timber.i("getLocationUpdates() is called")
        return _locationUpdates
    }

    companion object {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            DEFAULT_INTERVAL_MILLIS
        ).build()
    }
}