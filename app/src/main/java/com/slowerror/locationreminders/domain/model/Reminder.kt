package com.slowerror.locationreminders.domain.model

import com.slowerror.locationreminders.data.local.entity.ReminderEntity

data class Reminder(
    val id: Long,
    val title: String,
    val description: String,
    val namePoi: String,
    val latitude: Double,
    val longitude: Double,
    val userId: String?
)

fun Reminder.toData(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        namePoi = namePoi,
        latitude = latitude,
        longitude = longitude,
        userId =  userId
    )
}
