package com.vassev.meetingapp.presentation.register

data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val location: String = ""
)
