package com.vassev.meetingapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.domain.repository.AuthRepository
import com.vassev.meetingapp.domain.requests.RegisterRequest
import com.vassev.meetingapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewodel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<Resource<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.EmailChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        email = event.newEmail
                    )
                 }
            }
            is RegisterEvent.PasswordChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        password = event.newPassword
                    )
                }
            }
            is RegisterEvent.NameChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        name = event.newName
                    )
                }
            }
            is RegisterEvent.LocationChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        location = event.newLocation
                    )
                }
            }
            is RegisterEvent.RegisterButtonClicked -> {
                register()
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val result = authRepository.register(
                RegisterRequest(
                    email = state.value.email,
                    password = state.value.password,
                    name = state.value.name,
                    location = state.value.location,
                )
            )
            resultChannel.send(result)
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val result = authRepository.authenticate()
            resultChannel.send(result)
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false
                )
            }
        }
    }
}