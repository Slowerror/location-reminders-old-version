package com.slowerror.locationreminders.presentation.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import com.slowerror.locationreminders.R
import timber.log.Timber

class RegisterRequestPermissions(
    private val context: Context,
    private val locationPermissionRequest: ActivityResultLauncher<Array<String>>,
    private val view: View
) {

    fun processPermissions(permissions: Map<String, Boolean>) {
        when {
            permissions.getOrDefaultPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Timber.i("fine location is called")
            }
            permissions.getOrDefaultPermission(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                Timber.i("coarse location is called")

                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.dialog_title_access_fine_location))
                    .setMessage(context.getString(R.string.dialog_message_access_coarse_location))
                    .setPositiveButton(context.getString(R.string.dialog_positive_button_configure)) { dialogInterface, _ ->
                        val intent = getAppSettingsIntent()
                        startActivity(context, intent, null)
                        dialogInterface.dismiss()
                    }.create().show()
            }
            else -> {
                Timber.i("permissions denied")

                val intent = getAppSettingsIntent()
                Snackbar.make(
                    view,
                    context.getString(R.string.snackbar_message_no_access_location),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(context.getString(R.string.settings)) {
                        startActivity(context, intent, null)
                    }.show()
            }
        }
    }

    private fun getAppSettingsIntent(): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package:" + context.packageName)
        }
    }

    fun checkPermissions() {
        when {
            context.hasLocationPermissions() -> {
                Timber.i("Пермишены разрешены")
            }
            shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                Timber.i("Пермишены запрещены coarse")

                /*val intent = getAppSettingsIntent()
                Snackbar.make(requireView(), "У приложения нет доступа к местоположению", Snackbar.LENGTH_LONG)
                    .setAction("Настройки") {
                        startActivity(intent)
                    }.show()*/

                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.dialog_title_need_your_request))
                    .setMessage(context.getString(R.string.dialog_message_permission_rationale))
                    .setNegativeButton(context.getString(R.string.dialog_negative_button_no_thanks)) { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
                    .setPositiveButton(context.getString(R.string.dialog_positive_button_configure)) { dialogInterface, _ ->
                        locationPermissionRequest.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                        dialogInterface.dismiss()
                    }.create().show()
            }
            else -> {
                Timber.i(context.getString(R.string.request_permissions))
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
    }
}