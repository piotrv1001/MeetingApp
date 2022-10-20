package com.vassev.meetingapp.presentation.calendar

import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.SpecificDay

data class CalendarState(
    val isLoading: Boolean = false,
    val plans: List<Plan> = emptyList()
)
