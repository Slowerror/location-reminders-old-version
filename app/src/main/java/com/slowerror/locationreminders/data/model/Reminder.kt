package com.slowerror.locationreminders.data.model

data class Reminder(
    val id: Long,
    val title: String,
    val description: String,
    val marker: Marker,
    val userId: String?
)
