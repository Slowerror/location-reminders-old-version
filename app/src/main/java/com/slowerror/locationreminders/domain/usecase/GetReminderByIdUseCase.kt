package com.slowerror.locationreminders.domain.usecase

import com.slowerror.locationreminders.domain.repository.ReminderRepository
import javax.inject.Inject

class GetReminderByIdUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(reminderId: Long) {
        reminderRepository.getReminderById(reminderId)
    }

}