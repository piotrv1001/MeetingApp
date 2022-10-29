package com.vassev.meetingapp.presentation.generate_time

import com.vassev.meetingapp.domain.responses.GenerateMeetingTimeResponse

data class GenerateTimeState(
    val isLoading: Boolean = false,
    val generatedTimes: List<GenerateMeetingTimeResponse> = emptyList(),
    val numberOfWeeks: Int = 1,
    val numberOfResults: Int = 0,
    val preferredTime: Int = 0
)
