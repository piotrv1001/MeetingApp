package com.vassev.meetingapp.domain.requests

import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.SpecificDay
import kotlinx.serialization.Serializable

@Serializable
data class OneTimePlanRequest(
    val specificDay: SpecificDay,
    val userId: String,
    val plans: List<Plan> = emptyList()
)

