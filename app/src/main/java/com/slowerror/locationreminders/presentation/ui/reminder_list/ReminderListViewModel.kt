package com.slowerror.locationreminders.presentation.ui.reminder_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slowerror.locationreminders.domain.model.Reminder

class ReminderListViewModel : ViewModel() {

    private val reminder = Reminder(
        1L,
        "title",
        "desc",
        "Location",
        1.0,
        1.0,
        "userId"
    )

    private val listReminder = mutableListOf<Reminder>()

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders = _reminders

    init {
        for (i in 0..12) {
            listReminder.add(i, reminder)
        }

        _reminders.value = listReminder.toList()
    }
}