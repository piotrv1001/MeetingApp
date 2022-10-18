package com.vassev.meetingapp.domain.requests

import kotlinx.serialization.Serializable

@Serializable
data class UsersForMeetingRequest(
    val userIds: List<String>
)
