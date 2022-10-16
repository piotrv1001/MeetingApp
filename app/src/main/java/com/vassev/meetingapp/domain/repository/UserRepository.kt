package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.model.User
import com.vassev.meetingapp.domain.requests.UpdateUserWithMeetingRequest
import com.vassev.meetingapp.domain.util.Constants
import com.vassev.meetingapp.domain.util.Resource

interface UserRepository {

    suspend fun getUserById(userId: String): User

    suspend fun getUsersForMeeting(meetingId: String): List<UserDTO>

    suspend fun addUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun getAllAppUsers(): List<UserDTO>

    suspend fun updateUsersWithMeeting(request: UpdateUserWithMeetingRequest): Resource<Unit>

    sealed class Endpoints(val url: String) {
        object User: Endpoints("${Constants.BASE_URL}/user")
    }
}