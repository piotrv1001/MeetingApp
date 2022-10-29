package com.vassev.meetingapp.domain.requests

import kotlinx.serialization.Serializable

@Serializable
data class GenerateTimeRequest(
    val numberOfWeeks: Int,
    val numberOfResults: Int,
    val preferredTime: Int
)

