package com.vassev.meetingapp.domain.responses

import com.vassev.meetingapp.data.remote.dto.OneTimePlanDTO
import com.vassev.meetingapp.data.remote.dto.RepeatedPlanDTO
import kotlinx.serialization.Serializable

@Serializable
data class PlanResponse(
    val oneTimePlan: OneTimePlanDTO?,
    val repeatedPlan: RepeatedPlanDTO?
)

