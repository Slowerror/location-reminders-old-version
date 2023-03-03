package com.slowerror.locationreminders.data.repository

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.data.local.dao.ReminderDao
import com.slowerror.locationreminders.data.mapper.ReminderMapper
import com.slowerror.locationreminders.di.scope.IoDispatcher
import com.slowerror.locationreminders.domain.model.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderMapper: ReminderMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun getReminders(): Flow<List<Reminder>> {
        return reminderDao.getReminders().map { list ->
            list.map { item ->
                reminderMapper.mapToDomain(item)
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun saveReminder(reminder: Reminder) = withContext(ioDispatcher) {
        reminderDao.saveReminder(reminderMapper.mapToData(reminder))
    }

    override suspend fun removeAllReminders() = withContext(ioDispatcher) {
        reminderDao.deleteAllReminders()
    }

    override suspend fun removeReminder(reminder: Reminder) = withContext(ioDispatcher) {
        reminderDao.deleteReminder(reminderMapper.mapToData(reminder))
    }

    override suspend fun getReminderById(reminderId: Long): Resource<Reminder> =
        withContext(ioDispatcher) {
            return@withContext try {
                Resource.Success(
                    reminderMapper.mapToDomain(
                        reminderDao.getReminderById(reminderId = reminderId)
                    )
                )
            } catch (e: IOException) {
                Resource.Error(message = "Получена ошибка: $e")
            }

        }

}