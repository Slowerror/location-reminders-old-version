package com.slowerror.locationreminders.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.slowerror.locationreminders.data.local.entity.ReminderEntity
import com.slowerror.locationreminders.data.local.dao.ReminderDao

@Database(version = 1, entities = [ReminderEntity::class], exportSchema = false)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderDao? = null

        fun getInstance(context: Context): ReminderDao {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReminderDatabase::class.java,
                        "locationReminders.db"
                    ).build().reminderDao()
                }

                return instance
            }
        }
    }
}