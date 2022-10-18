package com.vassev.meetingapp.presentation.chat

sealed class ChatEvent {
    object JoinChatRoom: ChatEvent()
    object LeaveChatRoom: ChatEvent()
    object SendMessage: ChatEvent()
    data class MessageChanged(val newMessage: String): ChatEvent()
}
