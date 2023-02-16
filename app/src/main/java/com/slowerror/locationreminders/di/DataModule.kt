package com.slowerror.locationreminders.di

import android.content.Context
import com.slowerror.locationreminders.data.local.ReminderDatabase
import com.slowerror.locationreminders.data.local.dao.ReminderDao
import com.slowerror.locationreminders.data.mapper.ReminderMapper
import com.slowerror.locationreminders.data.repository.ReminderRepositoryImpl
import com.slowerror.locationreminders.domain.repository.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ReminderDatabase =
        ReminderDatabase.getInstance(context = context)


    @Provides
    fun provideDao(database: ReminderDatabase): ReminderDao = database.reminderDao()


    @Provides
    @Singleton
    fun provideReminderRepository(
        reminderDao: ReminderDao,
        reminderMapper: ReminderMapper
    ): ReminderRepository =
        ReminderRepositoryImpl(reminderDao = reminderDao, reminderMapper = reminderMapper)

}