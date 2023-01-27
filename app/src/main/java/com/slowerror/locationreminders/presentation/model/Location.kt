package com.slowerror.locationreminders.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    var title: String?,
    var lat: Double?,
    var lng: Double?
) : Parcelable
