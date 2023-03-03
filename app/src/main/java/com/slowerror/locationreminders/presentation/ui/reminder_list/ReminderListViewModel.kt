package com.slowerror.locationreminders.presentation.ui.reminder_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.usecase.GetRemindersUseCase
import com.slowerror.locationreminders.domain.usecase.RemoveAllRemindersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderListViewModel @Inject constructor(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val removeRemindersUseCase: RemoveAllRemindersUseCase
) : ViewModel() {

    private val reminder = Reminder(
        title = "title",
        description = "desc",
        namePoi = "Location",
        latitude = 1.0,
        longitude = 1.0
    )

    private var _uiState = MutableStateFlow<List<Reminder>?>(emptyList())
    val uiState: StateFlow<List<Reminder>?> = _uiState

    private val _reminders = MutableLiveData<List<Reminder>?>()
    val reminders = _reminders

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage = _errorMessage


    init {
        getReminders()
    }



    private fun getReminders() {
        viewModelScope.launch {
            getRemindersUseCase().collect { listReminder ->
                _uiState.value = listReminder
                /*listReminder.compareAndSet(it, it)
                _reminders.postValue(it)*/
            }
            /*val value = getRemindersUseCase()
//            val data = value.data
            if (value.data != null) {
                _reminders.value = value.data
            } else {
                _errorMessage.value = value.message
            }*/
        }
    }

    fun removeReminders() = viewModelScope.launch {
        removeRemindersUseCase()
    }
}