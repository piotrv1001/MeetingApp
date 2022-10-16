package com.vassev.meetingapp.data.remote.repository

import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.model.User
import com.vassev.meetingapp.domain.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.request.*

class UserRepositoryImpl(
    private val client: HttpClient
): UserRepository {

    override suspend fun getAllAppUsers(): List<UserDTO> {
        return try {
            client.get("http://${UserRepository.Endpoints.User.url}")
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getUserById(userId: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun getUsersForMeeting(meetingId: String): List<UserDTO> {
        return try {
            client.get("http://${UserRepository.Endpoints.User.url}?meetingId=$meetingId")
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        TODO("Not yet implemented")
    }
}