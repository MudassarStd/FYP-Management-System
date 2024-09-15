package com.android.cuifypmanagementsystem.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.Announcement
import com.android.cuifypmanagementsystem.repository.AnnouncementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {

    private val _publishResult = MutableLiveData<Result<Unit>>(Result.Loading)
    val publishResult: LiveData<Result<Unit>> get() = _publishResult

    fun publishAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            _publishResult.value = announcementRepository.publishAnnouncement(announcement)
        }
    }
}