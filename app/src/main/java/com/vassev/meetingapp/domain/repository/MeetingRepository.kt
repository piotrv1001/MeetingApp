package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.model.Meeting
import com.vassev.meetingapp.domain.requests.MeetingRequest
import com.vassev.meetingapp.domain.requests.MeetingsForUserRequest
import com.vassev.meetingapp.domain.requests.SaveMeetingTimeRequest
import com.vassev.meetingapp.domain.util.Constants
import com.vassev.meetingapp.domain.util.Resource

interface MeetingRepository {

    suspend fun getAllMeetingsForUser(meetingsForUserRequest: MeetingsForUserRequest): List<MeetingDTO>

    suspend fun getMeetingById(meetingId: String): MeetingDTO?

    suspend fun updateMeeting(meeting: MeetingDTO): Resource<Unit>

    suspend fun insertMeeting(meetingRequest: MeetingRequest): Resource<Unit>

    suspend fun saveMeetingTime(saveMeetingTimeRequest: SaveMeetingTimeRequest): Resource<Unit>

    suspend fun leaveMeeting(meetingId: String, userId: String): Resource<Unit>

    suspend fun deleteMeeting(meetingId: String): Resource<Unit>

    sealed class Endpoints(val url: String) {
        object Meeting: Endpoints("${Constants.BASE_URL}/meeting")
        object UpdateMeeting: Endpoints("${Constants.BASE_URL}/meeting/update")
        object SaveMeetingTime: Endpoints("${Constants.BASE_URL}/meeting/saveTime")
        object LeaveMeeting: Endpoints("${Constants.BASE_URL}/meeting/leave")
    }
}