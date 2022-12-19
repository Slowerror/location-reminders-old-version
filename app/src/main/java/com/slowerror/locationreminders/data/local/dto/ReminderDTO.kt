package com.slowerror.locationreminders.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.slowerror.locationreminders.domain.model.Reminder

@Entity(tableName = "reminders")
data class ReminderDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    val id: Long,

    val title: String,
    val description: String,

    @ColumnInfo(name = "name_poi")
    val namePoi: String,

    val latitude: Double,
    val longitude: Double,
    val userId: String?
)

fun ReminderDTO.toDomain(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        namePoi = namePoi
    )
}