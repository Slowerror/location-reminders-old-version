package com.slowerror.locationreminders.domain.usecase.get_reminders

import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.repository.ReminderRepository

class GetRemindersUseCase(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(): Resource<List<Reminder>> {
        return reminderRepository.getReminders()
    }

}