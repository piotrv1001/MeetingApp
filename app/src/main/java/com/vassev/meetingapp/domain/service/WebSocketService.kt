package com.vassev.meetingapp.domain.service

import com.vassev.meetingapp.domain.model.Message
import com.vassev.meetingapp.domain.requests.WebSocketRequest
import com.vassev.meetingapp.domain.util.Constants.BASE_URL
import com.vassev.meetingapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface WebSocketService {

    suspend fun startSession(
        webSocketRequest: WebSocketRequest
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    sealed class Endpoints(val url: String) {
        object Chat: Endpoints("$BASE_URL/chat")
    }
}