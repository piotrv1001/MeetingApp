package com.vassev.meetingapp.presentation.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(
    private val prefs: SharedPreferences
) : ViewModel() {

    private val resultChannel = Channel<String>()
    val settingsResults = resultChannel.receiveAsFlow()

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.LogOutButtonClicked -> {
                logOut()
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            prefs.edit()
                .remove("jwt")
                .apply()
            resultChannel.send("log_out")
        }
    }
}