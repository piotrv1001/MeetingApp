package com.vassev.meetingapp.presentation.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<String>()
    val settingsResults = resultChannel.receiveAsFlow()

    init {
        initializeDarkTheme()
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.LogOutButtonClicked -> {
                logOut()
            }
            is SettingsEvent.DarkThemeSwitchPressed -> {
                _state.update { currentState ->
                    currentState.copy(
                        isDarkThemeEnabled = event.checked
                    )
                }
                if(event.checked) {
                    prefs.edit()
                        .putInt("dark_mode", 1)
                        .apply()
                } else {
                    prefs.edit()
                        .remove("dark_mode")
                        .apply()
                }
            }
        }
    }

    private fun initializeDarkTheme() {
        _state.update { currentState ->
            currentState.copy(
                isDarkThemeEnabled = prefs.contains("dark_mode")
            )
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            prefs.edit()
                .remove("jwt")
                .remove("userId")
                .apply()
            resultChannel.send("log_out")
        }
    }
}