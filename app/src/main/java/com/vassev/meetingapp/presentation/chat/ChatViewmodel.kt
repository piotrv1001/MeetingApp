package com.vassev.meetingapp.presentation.chat

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.repository.MessageRepository
import com.vassev.meetingapp.domain.repository.UserRepository
import com.vassev.meetingapp.domain.requests.WebSocketRequest
import com.vassev.meetingapp.domain.service.WebSocketService
import com.vassev.meetingapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewmodel @Inject constructor(
    private val webSocketService: WebSocketService,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle,
    private val prefs: SharedPreferences
) : ViewModel() {

    val userId = prefs.getString("userId", "ERROR") ?: ""

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<Resource<Unit>>()
    val chatResults = resultChannel.receiveAsFlow()

    fun onEvent(event: ChatEvent) {
        when(event) {
            is ChatEvent.JoinChatRoom -> {
                joinChatRoom()
            }
            is ChatEvent.LeaveChatRoom -> {
                leaveChatRoom()
            }
            is ChatEvent.SendMessage -> {
                sendMessage()
            }
            is ChatEvent.MessageChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        messageText = event.newMessage
                    )
                }
            }
        }
    }

    private fun joinChatRoom() {
        val meetingId = savedStateHandle.get<String>("meetingId") ?: ""
        if(userId != "" && meetingId != "") {
            loadMeetingMessages(meetingId)
            viewModelScope.launch {
                val currentUser = userRepository.getUserById(userId)
                if(currentUser == null) {
                    resultChannel.send(Resource.Error("User not found"))
                } else {
                    val result = webSocketService.startSession(
                        WebSocketRequest(
                            userId = userId,
                            meetingId = meetingId,
                            username = currentUser.name
                        )
                    )
                    when(result) {
                        is Resource.Success -> {
                            webSocketService.observeMessages()
                                .onEach { message ->
                                    val newList = state.value.messages.toMutableList().apply {
                                        add(0, message)
                                    }
                                    _state.update { currentState ->
                                        currentState.copy(
                                            messages = newList
                                        )
                                    }
                                }.launchIn(viewModelScope)
                        }
                        is Resource.Error -> {
                            resultChannel.send(Resource.Error("Error connecting to chat"))
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun leaveChatRoom() {
        viewModelScope.launch {
            webSocketService.closeSession()
        }
    }

    private fun loadMeetingMessages(meetingId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val messages = messageRepository.getAllMessagesForMeeting(meetingId)
            messages.forEach { message ->
                Log.d("Message", message.text)
            }
            _state.update { currentState ->
                currentState.copy(
                    messages = messages,
                    isLoading = false
                )
            }
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            if(state.value.messageText.isNotBlank()) {
                webSocketService.sendMessage(state.value.messageText)
                _state.update { currentState ->
                    currentState.copy(
                        messageText = ""
                    )
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        leaveChatRoom()
    }


}