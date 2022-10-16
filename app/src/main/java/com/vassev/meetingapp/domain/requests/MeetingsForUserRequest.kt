package com.vassev.meetingapp.domain.requests

import kotlinx.serialization.Serializable

@Serializable
data class MeetingsForUserRequest(
    val meetingIds: List<String>
)

