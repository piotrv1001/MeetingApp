package com.vassev.meetingapp.domain.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserWithMeetingRequest(
    val userIds: List<String>,
    val meetingId: String
)

