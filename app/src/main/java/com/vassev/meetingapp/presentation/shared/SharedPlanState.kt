package com.vassev.meetingapp.presentation.shared

import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.SpecificDay

data class SharedPlanState(
    val isLoading: Boolean = false,
    val plans: List<Plan> = emptyList(),
    val specificDay: SpecificDay = SpecificDay(0, 0, 0),
    val fromHour: String = "",
    val fromMinute: String = "",
    val toHour: String = "",
    val toMinute: String = "",
    val isRepeatChecked: Boolean = false
)
