package com.slowerror.locationreminders.data.repository

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.data.local.dao.ReminderDao
import com.slowerror.locationreminders.data.local.dto.toDomain
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ReminderRepositoryImpl(
    private val reminderDao: ReminderDao,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : ReminderRepository {

    override suspend fun getReminders(): Resource<List<Reminder>> = withContext(dispatcherIO) {
        return@withContext try {
            Resource.Success(
                reminderDao.getReminders().map {
                    it.toDomain()
                }
            )
        } catch (e: IOException) {
            Resource.Error(message = "Получена ошибка: $e")
        }
    }

}