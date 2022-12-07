package com.vassev.meetingapp.presentation.generate_time

import com.vassev.meetingapp.domain.responses.GenerateMeetingTimeResponse

data class GenerateTimeState(
    val isLoading: Boolean = false,
    val isAlreadyGenerated: Boolean = false,
    val generatedTimes: List<GenerateMeetingTimeResponse> = emptyList(),
    val chosenTime: GenerateMeetingTimeResponse? = null,
    val numberOfWeeks: Int = 1,
    val numberOfResults: Int = 0,
    val showAllResults: Boolean = true
)
