package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.model.Meeting
import com.vassev.meetingapp.domain.requests.MeetingRequest
import com.vassev.meetingapp.domain.util.Constants
import com.vassev.meetingapp.domain.util.Resource

interface MeetingRepository {

    suspend fun getAllMeetingsForUser(userId: String): List<Meeting>

    suspend fun getMeetingById(meetingId: String): MeetingDTO?

    suspend fun updateMeeting(meeting: MeetingDTO): Resource<Unit>

    suspend fun insertMeeting(meetingRequest: MeetingRequest): String

    sealed class Endpoints(val url: String) {
        object Meeting: Endpoints("${Constants.BASE_URL}/meeting")
    }
}