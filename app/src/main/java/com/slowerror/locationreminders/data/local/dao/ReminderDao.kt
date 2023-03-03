package com.slowerror.locationreminders.data.local.dao

import androidx.room.*
import com.slowerror.locationreminders.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders")
    fun getReminders(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE entry_id = :reminderId")
    suspend fun getReminderById(reminderId: Long): ReminderEntity

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()

}