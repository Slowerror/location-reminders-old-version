package com.slowerror.locationreminders.di.module

import android.content.Context
import com.slowerror.locationreminders.data.local.ReminderDatabase
import com.slowerror.locationreminders.data.local.dao.ReminderDao
import com.slowerror.locationreminders.data.local.entity.ReminderEntity
import com.slowerror.locationreminders.data.mapper.Mapper
import com.slowerror.locationreminders.data.mapper.ReminderMapper
import com.slowerror.locationreminders.domain.model.Reminder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ReminderDatabase =
        ReminderDatabase.getInstance(context = context)

    @Provides
    fun provideDao(database: ReminderDatabase): ReminderDao = database.reminderDao()

    @Provides
    @Singleton
    fun provideReminderMapper(): ReminderMapper = ReminderMapper()

}