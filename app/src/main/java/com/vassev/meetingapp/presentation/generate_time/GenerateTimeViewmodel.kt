package com.vassev.meetingapp.presentation.generate_time

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.domain.requests.GenerateTimeRequest
import com.vassev.meetingapp.domain.service.GenerateMeetingTimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateTimeViewmodel @Inject constructor(
    private val generateMeetingTimeService: GenerateMeetingTimeService
) : ViewModel() {

    private val _state = MutableStateFlow(GenerateTimeState())
    val state = _state.asStateFlow()

    fun onEvent(event: GenerateTimeEvent) {
        when(event) {
            is GenerateTimeEvent.GenerateTimeButtonClicked -> {
                generateTime(event.meetingId)
            }
            is GenerateTimeEvent.SaveTimeButtonClicked -> {
                saveMeeting()
            }
            is GenerateTimeEvent.IncrementWeeks -> {
                _state.update { currentState ->
                    currentState.copy(
                        numberOfWeeks = currentState.numberOfWeeks + 1
                    )
                }
            }
            is GenerateTimeEvent.DecrementWeeks -> {
                _state.update { currentState ->
                    currentState.copy(
                        numberOfWeeks = currentState.numberOfWeeks - 1
                    )
                }
            }
            is GenerateTimeEvent.IncrementResults -> {
                _state.update { currentState ->
                    currentState.copy(
                        numberOfResults = currentState.numberOfResults + 1
                    )
                }
            }
            is GenerateTimeEvent.DecrementResults -> {
                _state.update { currentState ->
                    currentState.copy(
                        numberOfResults = currentState.numberOfResults - 1
                    )
                }
            }
            is GenerateTimeEvent.ChooseMorning -> {
                _state.update { currentState ->
                    currentState.copy(
                        preferredTime = 1
                    )
                }
            }
            is GenerateTimeEvent.ChooseAfternoon -> {
                _state.update { currentState ->
                    currentState.copy(
                        preferredTime = 2
                    )
                }
            }
            is GenerateTimeEvent.ChooseEvening -> {
                _state.update { currentState ->
                    currentState.copy(
                        preferredTime = 3
                    )
                }
            }
        }
    }

    private fun generateTime(meetingId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val generatedTimes = generateMeetingTimeService.generateMeetingTime(
                meetingId = meetingId,
                generateTimeRequest = GenerateTimeRequest(
                    numberOfResults = state.value.numberOfResults,
                    numberOfWeeks = state.value.numberOfWeeks,
                    preferredTime = state.value.preferredTime
                )
            )
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    generatedTimes = generatedTimes
                )
            }
        }
    }

    private fun saveMeeting() {

    }
}