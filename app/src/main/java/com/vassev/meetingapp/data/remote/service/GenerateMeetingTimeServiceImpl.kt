package com.vassev.meetingapp.data.remote.service

import com.vassev.meetingapp.domain.responses.GenerateMeetingTimeResponse
import com.vassev.meetingapp.domain.service.GenerateMeetingTimeService
import io.ktor.client.*
import io.ktor.client.request.*

class GenerateMeetingTimeServiceImpl(
    private val client: HttpClient
): GenerateMeetingTimeService {

    override suspend fun generateMeetingTime(meetingId: String): List<GenerateMeetingTimeResponse> {
        return try {
            client.get("http://${GenerateMeetingTimeService.Endpoints.Generate.url}/$meetingId")
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}