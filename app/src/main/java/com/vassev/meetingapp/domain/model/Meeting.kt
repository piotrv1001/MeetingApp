package com.vassev.meetingapp.domain.model

data class Meeting(
    val name: String,
    val duration: Int,
    val date: String,
    val location: String,
    val users: List<String> = emptyList()
)
