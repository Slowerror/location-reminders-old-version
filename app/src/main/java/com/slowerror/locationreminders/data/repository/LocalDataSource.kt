package com.slowerror.locationreminders.data.repository

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getReminders(): Flow<List<Reminder>>

    suspend fun saveReminder(reminder: Reminder)

    suspend fun removeAllReminders()

    suspend fun removeReminder(reminder: Reminder)

    suspend fun getReminderById(reminderId: Long): Resource<Reminder>
}