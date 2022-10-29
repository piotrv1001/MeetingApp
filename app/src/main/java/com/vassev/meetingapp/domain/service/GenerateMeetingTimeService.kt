package com.vassev.meetingapp.domain.service

import com.vassev.meetingapp.domain.requests.GenerateTimeRequest
import com.vassev.meetingapp.domain.responses.GenerateMeetingTimeResponse
import com.vassev.meetingapp.domain.util.Constants

interface GenerateMeetingTimeService {

    suspend fun generateMeetingTime(meetingId: String, generateTimeRequest: GenerateTimeRequest): List<GenerateMeetingTimeResponse>

    sealed class Endpoints(val url: String) {
        object Generate: Endpoints("${Constants.BASE_URL}/generateTime")
    }
}