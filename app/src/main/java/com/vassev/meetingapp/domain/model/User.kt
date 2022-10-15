package com.vassev.meetingapp.domain.model

data class User(
    val email: String,
    val password: String,
    val name: String,
    val location: String,
    val meetings: List<String> = emptyList()
)
