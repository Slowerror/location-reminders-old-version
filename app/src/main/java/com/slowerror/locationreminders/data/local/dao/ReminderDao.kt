package com.slowerror.locationreminders.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.slowerror.locationreminders.data.local.entity.ReminderEntity

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders")
    suspend fun getReminders(): List<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE entry_id = :reminderId")
    suspend fun getReminderById(reminderId: Long): ReminderEntity

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()

}