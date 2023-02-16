package com.slowerror.locationreminders.presentation.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun Context.hasLocationPermissions(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasGpsEnabled(): Boolean {
    val locationManager =
        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Map<String, Boolean?>.getOrDefaultPermission(key: String, defaultValue: Boolean = false): Boolean {
    return this.getValue(key) ?: defaultValue
}

fun Context.customDialog(
    title: String,
    message: String,
    posButton: String = "Настроить",
    positiveListener: DialogInterface.OnClickListener,
    negativeButton: String = "Закрыть",
    negativeListener: DialogInterface.OnClickListener
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(posButton, positiveListener)
        .setNegativeButton(negativeButton, negativeListener)
        .create().show()
}

fun View.snackBar(message: String, duration: Int = BaseTransientBottomBar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}