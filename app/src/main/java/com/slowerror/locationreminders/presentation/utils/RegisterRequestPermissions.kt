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
                    .setTitle("Разрешение на точное местоположение")
                    .setMessage("Приложению нужно знать точное местоположение, чтобы правильно расчитывать расстояние до отмеченных меток")
                    .setPositiveButton("Настроить") { dialogInterface, _ ->
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
                    "У приложения нет доступа к местоположению",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Настройки") {
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
                    .setTitle("Нужно ваше разрешение")
                    .setMessage("Приложению нужно знать ваше местоположение, чтобы отображать его на карте и расчитывать расстояние до отмеченных меток")
                    .setNegativeButton("Нет, спасибо") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
                    .setPositiveButton("Настроить") { dialogInterface, _ ->
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
                Timber.i("Запрос пермишенов")
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