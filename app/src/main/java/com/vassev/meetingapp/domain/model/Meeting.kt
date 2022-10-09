package com.vassev.meetingapp.domain.model

data class Meeting(
    val name: String,
    val duration: Int,
    val date: Long,
    val location: String
)
