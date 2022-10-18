package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.domain.model.Message
import com.vassev.meetingapp.domain.util.Constants

interface MessageRepository {

    suspend fun getAllMessagesForMeeting(meetingId: String): List<Message>

    sealed class Endpoints(val url: String) {
        object Message: Endpoints("${Constants.BASE_URL}/message")
    }

}