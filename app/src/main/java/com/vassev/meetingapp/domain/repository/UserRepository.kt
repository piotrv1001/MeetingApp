package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.model.User
import com.vassev.meetingapp.domain.util.Constants

interface UserRepository {

    suspend fun getUserById(userId: String): UserDTO?

    suspend fun getUsersForMeeting(meetingId: String): List<UserDTO>

    suspend fun addUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun getAllAppUsers(): List<UserDTO>

    sealed class Endpoints(val url: String) {
        object User: Endpoints("${Constants.BASE_URL}/user")
    }
}