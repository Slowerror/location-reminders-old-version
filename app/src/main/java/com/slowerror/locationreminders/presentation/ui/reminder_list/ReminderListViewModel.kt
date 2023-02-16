package com.slowerror.locationreminders.presentation.ui.reminder_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.usecase.get_reminders.GetRemindersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderListViewModel @Inject constructor(
    private val getRemindersUseCase: GetRemindersUseCase
) : ViewModel() {

    private val reminder = Reminder(
        title = "title",
        description = "desc",
        namePoi = "Location",
        latitude = 1.0,
        longitude = 1.0
    )

    private val listReminder = mutableListOf<Reminder>()

    private val _reminders = MutableLiveData<List<Reminder>?>()
    val reminders = _reminders

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage = _errorMessage


    init {
        getReminders()
    }

    private fun getReminders() {
        viewModelScope.launch {
            val value = getRemindersUseCase.invoke()
//            val data = value.data
            if (value.data != null) {
                _reminders.value = value.data
            } else {
                _errorMessage.value = value.message
            }
        }
    }
}