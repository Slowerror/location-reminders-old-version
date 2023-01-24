package com.slowerror.locationreminders.domain.model

data class Reminder(
    val id: Long = 0,
    val title: String?,
    val description: String?,
    val namePoi: String?,
    val latitude: Double?,
    val longitude: Double?
)
