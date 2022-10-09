package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.domain.model.User

interface UserRepository {

    suspend fun getUserById(userId: String): User

    suspend fun getUsersForMeeting(meetingId: String): List<User>

    suspend fun addUser(user: User)

    suspend fun updateUser(user: User)
}