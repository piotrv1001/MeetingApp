package com.vassev.meetingapp.domain.service

import com.vassev.meetingapp.domain.model.Message
import com.vassev.meetingapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface WebSocketService {

    suspend fun startSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    companion object {

        private const val localIP = "localhost"
        // IP of the android emulator
        private const val emulatorIP = "10.0.2.2"

        const val BASE_URL = "ws://$localIP:8080"
    }

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}