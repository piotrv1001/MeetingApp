package com.vassev.meetingapp.presentation.meeting_info

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.repository.MessageRepository
import com.vassev.meetingapp.domain.service.GenerateMeetingTimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingInfoViewmodel @Inject constructor(
    private val prefs: SharedPreferences,
    private val meetingRepository: MeetingRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MeetingInfoState())
    val state = _state.asStateFlow()

    val userId = prefs.getString("userId", "ERROR") ?: ""

    fun onEvent(event: MeetingInfoEvent) {
        when(event) {
            is MeetingInfoEvent.LoadMeetingData -> {
                loadMeetingData(event.meetingId)
            }
        }
    }

    private fun loadMeetingData(meetingId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoadingMeetingInfo = true
                )
            }
            val meetingDTO = meetingRepository.getMeetingById(meetingId)
            _state.update { currentState ->
                currentState.copy(
                    meetingDTO = meetingDTO
                )
            }
            loadLastMeetingMessage(meetingId)
        }
    }

    private suspend fun loadLastMeetingMessage(meetingId: String) {
        val lastMeetingMessage = messageRepository.getLastMessageForMeeting(meetingId)
        if(lastMeetingMessage != null) {
            var messageText = ""
            messageText = if(lastMeetingMessage.text.length > 25) {
                lastMeetingMessage.text.take(25) + "..."
            } else {
                lastMeetingMessage.text
            }
            var messageToDisplay = ""
            messageToDisplay = if(userId == lastMeetingMessage.userId) {
                "You: $messageText"
            } else {
                "${lastMeetingMessage.username}: $messageText"
            }
            _state.update { currentState ->
                currentState.copy(
                    lastMeetingMessage = messageToDisplay,
                    isLoadingMeetingInfo = false
                )
            }
        } else {
            _state.update { currentState ->
                currentState.copy(
                    lastMeetingMessage = "No messages exchanged yet",
                    isLoadingMeetingInfo = false
                )
            }
        }
    }
    
}