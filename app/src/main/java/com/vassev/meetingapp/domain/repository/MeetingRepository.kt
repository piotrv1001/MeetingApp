package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.domain.model.Meeting

interface MeetingRepository {

    suspend fun getAllMeetingsForUser(userId: String): List<Meeting>

    suspend fun getMeetingById(meetingId: String): Meeting

    suspend fun updateMeeting(meetingId: String)
}