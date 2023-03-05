package com.slowerror.locationreminders.presentation.ui.reminder_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.usecase.GetRemindersUseCase
import com.slowerror.locationreminders.domain.usecase.RemoveAllRemindersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ReminderListViewModel @Inject constructor(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val removeRemindersUseCase: RemoveAllRemindersUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<Resource<List<Reminder>?>>(Resource.Loading(emptyList()))
    val uiState: StateFlow<Resource<List<Reminder>?>> = _uiState.asStateFlow()

    init {
        getReminders()
    }

    private fun getReminders() {
        Timber.i("getReminders() was called")
        viewModelScope.launch {
            Timber.i("getReminders().launch was called")
            getRemindersUseCase().collect { response ->
                Timber.i("collect was called")
                when (response) {
                    is Resource.Error -> {
                        Timber.i("Resource.Error was called ${response.message}")
                        _uiState.value = Resource.Error(response.message)
                    }
                    is Resource.Success -> {
                        Timber.i("collect: Resource.Success was called")
                        _uiState.value = Resource.Success(response.data)
                    }
                    is Resource.Loading -> {
                        Timber.i("collect: Resource.Loading was called")
                        _uiState.value = Resource.Loading()
                    }
                }

            }

        }

    }

    fun removeReminders() = viewModelScope.launch {
        try {
            Timber.i("removeReminders was called")
            removeRemindersUseCase()
        } catch (e: Throwable) {
            Timber.i("removeReminders was called Throwable")
            e.printStackTrace()
        }

    }
}
