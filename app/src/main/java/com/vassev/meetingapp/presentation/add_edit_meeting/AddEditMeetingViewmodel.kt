package com.vassev.meetingapp.presentation.add_edit_meeting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.requests.MeetingRequest
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMeetingViewmodel @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditMeetingState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<Resource<Unit>>()
    val meetingResults = resultChannel.receiveAsFlow()

    private var currentMeetingId: String? = null

    init {
        loadDataIfEdit()
    }

    fun onEvent(event: AddEditMeetingEvent) {
        when(event) {
            is AddEditMeetingEvent.TitleChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        title = event.newTitle
                    )
                }
            }
            is AddEditMeetingEvent.HoursChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        hours = event.newHours
                    )
                }
            }
            is AddEditMeetingEvent.MinutesChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        minutes = event.newMinutes
                    )
                }
            }
            is AddEditMeetingEvent.LocationChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        location = event.newLocation
                    )
                }
            }
            is AddEditMeetingEvent.SaveButtonClicked -> {
                save()
            }
        }
    }

    private fun save() {
        if(currentMeetingId == null) {
            val meetingRequest = MeetingRequest(
                name = state.value.title,
                location = state.value.location,
                duration = state.value.hours.toInt() * 60 + state.value.minutes.toInt(),
                // change date later
                date = 0
            )
            viewModelScope.launch {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = true
                    )
                }
                val result = meetingRepository.insertMeeting(meetingRequest)
                resultChannel.send(result)
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadDataIfEdit() {
        savedStateHandle.get<String>("meetingId")?.let { meetingId ->
            if(meetingId != "") {
                viewModelScope.launch {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = true
                        )
                    }
                    currentMeetingId = meetingId
                    meetingRepository.getMeetingById(meetingId)?.also { meetingDTO ->
                        val hours: Int = meetingDTO.duration / 60
                        val minutes: Int = meetingDTO.duration % 60
                        _state.update { currentState ->
                            currentState.copy(
                                title = meetingDTO.name,
                                location = meetingDTO.location,
                                hours = hours.toString(),
                                minutes = minutes.toString()
                            )
                        }
                    }
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}