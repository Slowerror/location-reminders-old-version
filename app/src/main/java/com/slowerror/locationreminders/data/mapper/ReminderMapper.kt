package com.slowerror.locationreminders.data.mapper

import com.slowerror.locationreminders.data.local.entity.ReminderEntity
import com.slowerror.locationreminders.domain.model.Reminder
import dagger.hilt.InstallIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderMapper @Inject constructor() : Mapper<ReminderEntity, Reminder> {

    override fun mapToDomain(type: ReminderEntity): Reminder {
        return Reminder(
            id = type.id,
            title = type.title,
            description = type.description,
            namePoi = type.namePoi,
            latitude = type.latitude,
            longitude = type.longitude
        )
    }

    override fun mapToData(type: Reminder): ReminderEntity {
        return ReminderEntity(
            id = type.id,
            title = type.title,
            description = type.description,
            namePoi = type.namePoi,
            latitude = type.latitude,
            longitude = type.longitude
        )
    }
}