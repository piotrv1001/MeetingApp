package com.vassev.meetingapp.data.remote.repository

import com.vassev.meetingapp.data.remote.dto.MessageDTO
import com.vassev.meetingapp.domain.model.Message
import com.vassev.meetingapp.domain.repository.MessageRepository
import io.ktor.client.*
import io.ktor.client.request.*

class MessageRepositoryImpl(
    private val client: HttpClient
): MessageRepository {

    override suspend fun getAllMessagesForMeeting(meetingId: String): List<Message> {
        return try {
            client.get<List<MessageDTO>>("http://${MessageRepository.Endpoints.Message.url}/$meetingId").sortedByDescending { it.timestamp }.map { it.fromDTOToEntity() }
        } catch(e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}