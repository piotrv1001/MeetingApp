package com.vassev.meetingapp.presentation.generate_time

import com.vassev.meetingapp.domain.responses.GenerateMeetingTimeResponse

sealed class GenerateTimeEvent {
    data class GenerateTimeButtonClicked(val meetingId: String): GenerateTimeEvent()
    data class SaveTimeButtonClicked(val meetingId: String): GenerateTimeEvent()
    data class ChooseMeetingTime(val chosenTime: GenerateMeetingTimeResponse): GenerateTimeEvent()
    object IncrementWeeks: GenerateTimeEvent()
    object DecrementWeeks: GenerateTimeEvent()
    object IncrementResults: GenerateTimeEvent()
    object DecrementResults: GenerateTimeEvent()
    data class ShowAllResultsCheckboxClicked(val checked: Boolean): GenerateTimeEvent()
}
