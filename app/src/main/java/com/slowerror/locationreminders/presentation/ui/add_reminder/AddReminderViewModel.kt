package com.slowerror.locationreminders.presentation.ui.add_reminder


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slowerror.locationreminders.domain.model.Reminder
import com.slowerror.locationreminders.presentation.model.Location
import timber.log.Timber


class AddReminderViewModel : ViewModel() {

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

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> = _location

    private var reminder: Reminder

    init {
        reminder = Reminder(
            id = 1L,
            title = titleReminder.value,
            description = descriptionReminder.value,
            namePoi = nameMarker.value,
            latitude = lat.value,
            longitude = lng.value
        )
    }

    fun getMarker(location: Location) {
        Timber.i("${location.title}")
        _location.value = location
    }

    fun save(name: String?, desc: String?) {

    }

    fun clearLocation() {
        _location.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared is called")
    }
}