package com.slowerror.locationreminders.domain.usecase

import com.slowerror.locationreminders.domain.repository.ReminderRepository
import javax.inject.Inject

class RemoveAllRemindersUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke() {
        reminderRepository.removeAllReminders()
    }
}