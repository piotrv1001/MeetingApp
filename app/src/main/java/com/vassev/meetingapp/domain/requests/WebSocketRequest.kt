package com.vassev.meetingapp.domain.requests

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketRequest(
    val userId: String,
    val meetingId: String
)
