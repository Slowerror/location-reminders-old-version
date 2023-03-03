package com.slowerror.locationreminders.domain.usecase

import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.repository.ReminderRepository
import javax.inject.Inject

class RemoveReminderUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) {
        reminderRepository.removeReminder(reminder)
    }

}