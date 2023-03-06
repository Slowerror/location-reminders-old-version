package com.slowerror.locationreminders.data.repository

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.data.local.dao.ReminderDao
import com.slowerror.locationreminders.data.mapper.ReminderMapper
import com.slowerror.locationreminders.di.scope.IoDispatcher
import com.slowerror.locationreminders.domain.model.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderMapper: ReminderMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun getReminders(): Flow<Resource<List<Reminder>>> = flow {
        try {
            val flow = reminderDao.getReminders().flowOn(ioDispatcher)

            flow.collect {
                emit(Resource.Loading())
                delay(1500)
                emit(Resource.Success(it.map { reminderEntity ->
                    reminderMapper.mapToDomain(reminderEntity)
                }))
            }
        } catch (cause: Throwable) {
            cause.printStackTrace()
            delay(1500)
            emit(Resource.Error(cause.message))
        }

        /*reminderDao.getReminders()
            .onEach {
                emit(Resource.Loading())
                delay(1500)

                emit(Resource.Success(it.map { reminderEntity ->
                    reminderMapper.mapToDomain(reminderEntity)
                }))
            }
            .catch { cause: Throwable ->
                delay(1500)
                emit(Resource.Error(cause.message))
            }
            .flowOn(ioDispatcher)
            .collect()*/

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

    override fun getReminderById(reminderId: Long): Flow<Resource<Reminder>> = flow {
        try {
            val flow = reminderDao.getReminderById(reminderId).flowOn(ioDispatcher)

            flow.collect {
                emit(Resource.Loading())
                emit(Resource.Success(reminderMapper.mapToDomain(it)))
            }
        } catch (cause: Throwable) {
            cause.printStackTrace()
            emit(Resource.Error(cause.message))
        }
    }

}