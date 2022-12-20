package com.slowerror.locationreminders.domain.repository

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder

interface ReminderRepository {

    suspend fun getReminders(): Resource<List<Reminder>>

    suspend fun saveReminder(reminder: Reminder)
}