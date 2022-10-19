package com.vassev.meetingapp.data.remote.service

import com.vassev.meetingapp.data.remote.dto.MessageDTO
import com.vassev.meetingapp.domain.model.Message
import com.vassev.meetingapp.domain.requests.WebSocketRequest
import com.vassev.meetingapp.domain.service.WebSocketService
import com.vassev.meetingapp.domain.util.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class WebSocketServiceImpl(
    private val client: HttpClient
): WebSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun startSession(webSocketRequest: WebSocketRequest): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("ws://${WebSocketService.Endpoints.Chat.url}?userId=${webSocketRequest.userId}&meetingId=${webSocketRequest.meetingId}&username=${webSocketRequest.username}")
            }
            if(socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Error("Could not establish connection")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDTO = Json.decodeFromString<MessageDTO>(json)
                    messageDTO.fromDTOToEntity()
                } ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}