package com.vassev.meetingapp.domain.responses

import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.SpecificDay
import kotlinx.serialization.Serializable

@Serializable
data class GenerateMeetingTimeResponse(
    val specificDay: SpecificDay,
    val plan: Plan
)

