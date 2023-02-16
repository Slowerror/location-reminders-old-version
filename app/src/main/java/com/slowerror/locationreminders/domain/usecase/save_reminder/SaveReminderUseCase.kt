package com.slowerror.locationreminders.domain.usecase.save_reminder

import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.repository.ReminderRepository

class SaveReminderUseCase(private val repository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) {
        repository.saveReminder(reminder)
    }

}