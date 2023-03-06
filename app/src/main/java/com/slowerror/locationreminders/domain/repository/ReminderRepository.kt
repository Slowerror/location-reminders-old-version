package com.slowerror.locationreminders.domain.repository

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getReminders(): Flow<Resource<List<Reminder>>>

    fun getReminderById(reminderId: Long): Flow<Resource<Reminder>>

    suspend fun saveReminder(reminder: Reminder)

    suspend fun removeAllReminders()

    suspend fun removeReminder(reminder: Reminder)
}