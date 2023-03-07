package com.slowerror.locationreminders.presentation.ui.reminder_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.usecase.GetReminderByIdUseCase
import com.slowerror.locationreminders.domain.usecase.RemoveReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReminderDetailsViewModel @Inject constructor(
    private val getReminderByIdUseCase: GetReminderByIdUseCase,
    private val removeReminderUseCase: RemoveReminderUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<Resource<Reminder?>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()


    fun getReminderById(id: Long) = viewModelScope.launch {
        getReminderByIdUseCase(id).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.value = Resource.Error(result.message)
                }
                is Resource.Loading -> {
                    _uiState.value = Resource.Loading()
                }
                is Resource.Success -> {
                    _uiState.value = Resource.Success(result.data)
                }
            }
        }
    }

    suspend fun removeReminder() = viewModelScope.launch {
        _uiState.value.data?.let {
            Timber.i("removeReminder() was called")
            removeReminderUseCase(it)
        }
    }.join()

}