package com.vassev.meetingapp.presentation.meeting_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.service.GenerateMeetingTimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingInfoViewmodel @Inject constructor(
    private val meetingRepository: MeetingRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MeetingInfoState())
    val state = _state.asStateFlow()

    fun onEvent(event: MeetingInfoEvent) {
        when(event) {
            is MeetingInfoEvent.LoadMeetingData -> {
                loadMeetingData(event.meetingId)
            }
        }
    }

    private fun loadMeetingData(meetingId: String) {
        viewModelScope.launch{
            _state.update { currentState ->
                currentState.copy(
                    isLoadingMeetingInfo = true
                )
            }
            val meetingDTO = meetingRepository.getMeetingById(meetingId)
            _state.update { currentState ->
                currentState.copy(
                    meetingDTO = meetingDTO,
                    isLoadingMeetingInfo = false
                )
            }
        }
    }
    
}