package com.vassev.meetingapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.domain.repository.AuthRepository
import com.vassev.meetingapp.domain.requests.LoginRequest
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<Resource<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        email = event.newEmail
                    )
                }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        password = event.newPassword
                    )
                }
            }
            is LoginEvent.LoginButtonClicked -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val result = authRepository.login(
                LoginRequest(
                    email = state.value.email,
                    password = state.value.password
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
}