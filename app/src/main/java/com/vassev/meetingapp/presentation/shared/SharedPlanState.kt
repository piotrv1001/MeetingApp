package com.vassev.meetingapp.presentation.shared

import com.vassev.meetingapp.domain.model.PlanWithType
import com.vassev.meetingapp.domain.model.SpecificDay

data class SharedPlanState(
    val isLoading: Boolean = false,
    val plans: List<PlanWithType> = emptyList(),
    val specificDay: SpecificDay = SpecificDay(0, 0, 0),
    val fromHour: String = "",
    val fromMinute: String = "",
    val toHour: String = "",
    val toMinute: String = "",
    val isRepeatChecked: Boolean = false,
    val showDialog: Boolean = false,
    val currentPLan: PlanWithType = PlanWithType(0, 0, 0, 0, "",false),
    val removeEveryWeek: Boolean = false
)
