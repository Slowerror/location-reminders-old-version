package com.slowerror.locationreminders.presentation.ui.add_reminder


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.domain.usecase.SaveReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddReminderViewModel @Inject constructor(
    private val saveReminderUseCase: SaveReminderUseCase
) : ViewModel() {

    private val _titleReminder = MutableLiveData<String?>()
    val titleReminder: LiveData<String?> = _titleReminder

    private val _descriptionReminder = MutableLiveData<String?>()
    val descriptionReminder: LiveData<String?> = _descriptionReminder

    private val _nameMarker = MutableLiveData<String?>()
    val nameMarker: LiveData<String?> = _nameMarker

    private val _lat = MutableLiveData<Double?>()
    val lat: LiveData<Double?> = _lat

    private val _lng = MutableLiveData<Double?>()
    val lng: LiveData<Double?> = _lng


    fun saveMarker(title: String?, lat: Double?, lng: Double?) {
        Timber.i("AddReminderViewModel getMarker: $title")
        _nameMarker.value = title
        _lat.value = lat
        _lng.value = lng
    }


    suspend fun saveReminder(title: String, description: String) {
        Timber.i("saveReminder was called")
        val reminder = Reminder(
            title = title,
            description = description,
            namePoi = nameMarker.value,
            latitude = lat.value,
            longitude = lng.value
        )

        viewModelScope.launch {
            saveReminderUseCase(reminder)
        }.join()
    }
}