package com.slowerror.locationreminders.domain.usecase

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    operator fun invoke(): Flow<Resource<List<Reminder>>> {
        return reminderRepository.getReminders()
    }

}