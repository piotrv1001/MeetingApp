package com.vassev.meetingapp.data.remote.service

import com.vassev.meetingapp.domain.requests.GenerateTimeRequest
import com.vassev.meetingapp.domain.responses.GenerateMeetingTimeResponse
import com.vassev.meetingapp.domain.service.GenerateMeetingTimeService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class GenerateMeetingTimeServiceImpl(
    private val client: HttpClient
): GenerateMeetingTimeService {

    override suspend fun generateMeetingTime(meetingId: String, generateTimeRequest: GenerateTimeRequest): List<GenerateMeetingTimeResponse> {
        return try {
            client.get("http://${GenerateMeetingTimeService.Endpoints.Generate.url}/$meetingId") {
                contentType(ContentType.Application.Json)
                body = generateTimeRequest
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}