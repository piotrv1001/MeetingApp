package com.vassev.meetingapp.presentation.chat

import com.vassev.meetingapp.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val messageText: String = ""
)
