package com.vassev.meetingapp.data.remote.repository

import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.model.User
import com.vassev.meetingapp.domain.repository.UserRepository
import com.vassev.meetingapp.domain.requests.UsersForMeetingRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

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

    override suspend fun getUserById(userId: String): UserDTO? {
        return try {
            client.get("http://${UserRepository.Endpoints.User.url}/$userId")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getUsersForMeeting(usersForMeetingRequest: UsersForMeetingRequest): List<UserDTO> {
        return try {
            client.get("http://${UserRepository.Endpoints.User.url}/forMeeting") {
                contentType(ContentType.Application.Json)
                body = usersForMeetingRequest
            }
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