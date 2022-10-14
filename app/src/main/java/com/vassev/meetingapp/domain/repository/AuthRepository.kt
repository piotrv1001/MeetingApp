package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.domain.requests.LoginRequest
import com.vassev.meetingapp.domain.requests.RegisterRequest
import com.vassev.meetingapp.domain.util.Constants.BASE_URL
import com.vassev.meetingapp.domain.util.Resource

interface AuthRepository {

    suspend fun login(loginRequest: LoginRequest): Resource<Unit>

    suspend fun register(registerRequest: RegisterRequest): Resource<Unit>

    suspend fun authenticate(): Resource<Unit>

    sealed class Endpoints(val url: String) {
        object User: Endpoints("$BASE_URL/user")
    }
}