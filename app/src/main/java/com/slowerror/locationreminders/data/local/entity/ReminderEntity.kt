package com.slowerror.locationreminders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    val id: Long,

    val title: String?,
    val description: String?,

    @ColumnInfo(name = "name_poi")
    val namePoi: String?,

    val latitude: Double?,
    val longitude: Double?
)
