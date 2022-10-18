package com.vassev.meetingapp.presentation.meeting_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingInfoViewmodel @Inject constructor(
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _meeting = MutableStateFlow<MeetingDTO?>(null)
    val meeting = _meeting.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onEvent(event: MeetingInfoEvent) {
        when(event) {
            is MeetingInfoEvent.LoadMeetingData -> {
                loadMeetingData(event.meetingId)
            }
        }
    }

    private fun loadMeetingData(meetingId: String) {
        viewModelScope.launch{
            _isLoading.value = true
            _meeting.value = meetingRepository.getMeetingById(meetingId)
            _isLoading.value = false
        }
    }
    
}