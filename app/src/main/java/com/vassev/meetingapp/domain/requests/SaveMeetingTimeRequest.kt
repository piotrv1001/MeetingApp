package com.vassev.meetingapp.domain.requests

import com.vassev.meetingapp.domain.responses.GenerateMeetingTimeResponse
import kotlinx.serialization.Serializable

@Serializable
data class SaveMeetingTimeRequest(
    val meetingId: String,
    val generateTimeResponse: GenerateMeetingTimeResponse
)
