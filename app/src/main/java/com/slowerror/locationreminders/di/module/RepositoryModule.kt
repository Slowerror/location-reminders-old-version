package com.slowerror.locationreminders.di.module

import com.slowerror.locationreminders.data.repository.ReminderRepositoryImpl
import com.slowerror.locationreminders.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReminderRepository(reminderRepositoryImpl: ReminderRepositoryImpl): ReminderRepository

}