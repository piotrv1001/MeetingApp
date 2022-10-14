package com.vassev.meetingapp.presentation.register

sealed class RegisterEvent {
    data class EmailChanged(val newEmail: String): RegisterEvent()
    data class PasswordChanged(val newPassword: String): RegisterEvent()
    data class NameChanged(val newName: String): RegisterEvent()
    data class LocationChanged(val newLocation: String): RegisterEvent()
    object RegisterButtonClicked: RegisterEvent()
}
