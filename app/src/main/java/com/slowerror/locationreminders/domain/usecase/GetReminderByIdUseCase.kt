package com.slowerror.locationreminders.domain.usecase

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReminderByIdUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    operator fun invoke(reminderId: Long): Flow<Resource<Reminder>> {
        return reminderRepository.getReminderById(reminderId)
    }

}