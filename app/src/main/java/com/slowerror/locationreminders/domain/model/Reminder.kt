package com.slowerror.locationreminders.domain.model

data class Reminder(
    val id: Long,
    val title: String,
    val description: String,
    val namePoi: String
)
