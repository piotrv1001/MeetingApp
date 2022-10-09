package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.domain.model.Message

interface MessageRepository {

    suspend fun getAllMessagesForMeeting(meetingId: String): List<Message>

    suspend fun addMessage(message: Message)

}