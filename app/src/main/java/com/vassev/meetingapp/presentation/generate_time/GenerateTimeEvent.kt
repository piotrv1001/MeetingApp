package com.vassev.meetingapp.presentation.generate_time

sealed class GenerateTimeEvent {
    data class GenerateTimeButtonClicked(val meetingId: String): GenerateTimeEvent()
    object SaveTimeButtonClicked: GenerateTimeEvent()
    object IncrementWeeks: GenerateTimeEvent()
    object DecrementWeeks: GenerateTimeEvent()
    object IncrementResults: GenerateTimeEvent()
    object DecrementResults: GenerateTimeEvent()
    object ChooseMorning: GenerateTimeEvent()
    object ChooseAfternoon: GenerateTimeEvent()
    object ChooseEvening: GenerateTimeEvent()
}
