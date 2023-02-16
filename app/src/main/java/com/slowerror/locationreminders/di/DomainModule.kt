package com.slowerror.locationreminders.di

import com.slowerror.locationreminders.domain.repository.ReminderRepository
import com.slowerror.locationreminders.domain.usecase.get_reminders.GetRemindersUseCase
import com.slowerror.locationreminders.domain.usecase.save_reminder.SaveReminderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetReminders(reminderRepository: ReminderRepository): GetRemindersUseCase {
        return GetRemindersUseCase(reminderRepository = reminderRepository)
    }

    @Provides
    fun provideSaveReminder(reminderRepository: ReminderRepository): SaveReminderUseCase {
        return SaveReminderUseCase(repository = reminderRepository)
    }
}