package com.slowerror.locationreminders.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.slowerror.locationreminders.presentation.ui.select_location.SelectLocationFragment

class DefaultUserLocation(
    private val context: Context,
    private val activity: Activity,
    private val client: FusedLocationProviderClient
) {

    /*private fun currentLocation() {
        client.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                //                        lastLocation = location
                val lastLatLng = LatLng(location.latitude, location.longitude)

                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        lastLatLng,
                        SelectLocationFragment.DEFAULT_ZOOM_LEVEL
                    )
                )
            }
        }
    }*/

    fun locationSettings() {

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            DEFAULT_INTERVAL_MILLIS
        )

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest.build())

        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        activity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (_: IntentSender.SendIntentException) {

                }
            }
        }
    }

    companion object {
        private const val DEFAULT_INTERVAL_MILLIS = 10000L
        private const val REQUEST_CHECK_SETTINGS = 100
    }
}