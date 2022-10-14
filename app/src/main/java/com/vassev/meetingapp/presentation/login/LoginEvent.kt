package com.vassev.meetingapp.presentation.login

sealed class LoginEvent {
    data class EmailChanged(val newEmail: String): LoginEvent()
    data class PasswordChanged(val newPassword: String): LoginEvent()
    object LoginButtonClicked: LoginEvent()
}
