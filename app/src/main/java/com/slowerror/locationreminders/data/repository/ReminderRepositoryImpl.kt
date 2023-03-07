package com.slowerror.locationreminders.data.repository

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : ReminderRepository {

    override fun getReminders(): Flow<Resource<List<Reminder>>> = localDataSource.getReminders()

    override fun getReminderById(reminderId: Long): Flow<Resource<Reminder>> =
        localDataSource.getReminderById(reminderId)

    override suspend fun saveReminder(reminder: Reminder) = localDataSource.saveReminder(reminder)

    override suspend fun removeAllReminders() = localDataSource.removeAllReminders()

    override suspend fun removeReminder(reminder: Reminder) {
        localDataSource.removeReminder(reminder)
    }

}