package com.vassev.meetingapp.data.remote.repository

import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.model.Meeting
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.requests.MeetingRequest
import com.vassev.meetingapp.domain.requests.MeetingsForUserRequest
import com.vassev.meetingapp.domain.requests.SaveMeetingTimeRequest
import com.vassev.meetingapp.domain.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MeetingRepositoryImpl(
    private val client: HttpClient
): MeetingRepository {

    override suspend fun getAllMeetingsForUser(meetingsForUserRequest: MeetingsForUserRequest): List<MeetingDTO> {
        return try {
            client.get("http://${MeetingRepository.Endpoints.Meeting.url}/forUser") {
                contentType(ContentType.Application.Json)
                body = meetingsForUserRequest
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getMeetingById(meetingId: String): MeetingDTO? {
        return try {
            client.get("http://${MeetingRepository.Endpoints.Meeting.url}/$meetingId")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun updateMeeting(meeting: MeetingDTO): Resource<Unit> {
        return try {
            val response: HttpResponse = client.put("http://${MeetingRepository.Endpoints.UpdateMeeting.url}") {
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

    override suspend fun insertMeeting(meetingRequest: MeetingRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.post("http://${MeetingRepository.Endpoints.Meeting.url}") {
                contentType(ContentType.Application.Json)
                body = meetingRequest
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

    override suspend fun saveMeetingTime(saveMeetingTimeRequest: SaveMeetingTimeRequest): Resource<Unit> {
        return try{
            val response: HttpResponse = client.put("http://${MeetingRepository.Endpoints.SaveMeetingTime.url}") {
                contentType(ContentType.Application.Json)
                body = saveMeetingTimeRequest
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
}