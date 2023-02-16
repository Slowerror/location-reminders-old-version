package com.slowerror.locationreminders.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import timber.log.Timber

class GpsUtil(private val context: Context) {
    private val settingClient = LocationServices.getSettingsClient(context)
    private val locationSettingsRequest: LocationSettingsRequest

    init {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(UserLocationUtil.locationRequest)
            .setAlwaysShow(true)
        locationSettingsRequest = builder.build()
    }

    fun turnOnGps() {
        Timber.i("locationSettings was called")
        if (!context.hasGpsEnabled()) {
            settingClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnFailureListener { exception ->
                    Timber.i("locationSettings: addOnFailureListener was called")
                    if (exception is ResolvableApiException) {
                        try {
                            exception.startResolutionForResult(
                                context as Activity,
                                GPS_REQUEST_CHECK_SETTINGS
                            )
                        } catch (sendEx: IntentSender.SendIntentException) {
                            sendEx.printStackTrace()
                            Timber.e("Exception ${sendEx.message}")
                        }
                    }
                }
        }
    }

}