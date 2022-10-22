package com.vassev.meetingapp.domain.requests

import com.vassev.meetingapp.domain.model.SpecificDay
import kotlinx.serialization.Serializable

@Serializable
data class AddExceptionToRepeatedPlanRequest(
    val specificDay: SpecificDay,
    val userId: String,
    val dayOfWeek: Int
)
