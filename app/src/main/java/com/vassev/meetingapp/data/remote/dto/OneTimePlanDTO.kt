package com.vassev.meetingapp.data.remote.dto

import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.SpecificDay
import kotlinx.serialization.Serializable

@Serializable
data class OneTimePlanDTO(
    val oneTimePlanId: String,
    val specificDay: SpecificDay,
    val userId: String,
    val plans: List<Plan> = emptyList()
)
