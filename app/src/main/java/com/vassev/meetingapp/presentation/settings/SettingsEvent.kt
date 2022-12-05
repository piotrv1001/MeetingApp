package com.vassev.meetingapp.presentation.settings

sealed class SettingsEvent {
    object LogOutButtonClicked: SettingsEvent()
    data class DarkThemeSwitchPressed(val checked: Boolean): SettingsEvent()
}
