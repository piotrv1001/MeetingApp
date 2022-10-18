package com.vassev.meetingapp.domain.model

data class Message(
    val text: String,
    val formattedTime: String,
    val userId: String,
    val meetingId: String
)
