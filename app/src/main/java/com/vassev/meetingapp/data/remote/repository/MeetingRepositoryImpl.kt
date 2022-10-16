package com.vassev.meetingapp.data.remote.repository

import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.model.Meeting
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.requests.MeetingRequest
import com.vassev.meetingapp.domain.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MeetingRepositoryImpl(
    private val client: HttpClient
): MeetingRepository {

    override suspend fun getAllMeetingsForUser(userId: String): List<Meeting> {
        return try {
            val response: List<MeetingDTO> = client.get("http://${MeetingRepository.Endpoints.Meeting.url}?userId=$userId")
            response.map { it.fromDTOToEntity() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getMeetingById(meetingId: String): MeetingDTO? {
        return try {
            client.get("http://${MeetingRepository.Endpoints.Meeting.url}?meetingId=$meetingId")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun updateMeeting(meeting: MeetingDTO): Resource<Unit> {
        return try {
            val response: HttpResponse = client.put("http://${MeetingRepository.Endpoints.Meeting.url}") {
                contentType(ContentType.Application.Json)
                body = meeting
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }

    }

    override suspend fun insertMeeting(meetingRequest: MeetingRequest): String {
        return try {
            client.post("http://${MeetingRepository.Endpoints.Meeting.url}") {
                contentType(ContentType.Application.Json)
                body = meetingRequest
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}