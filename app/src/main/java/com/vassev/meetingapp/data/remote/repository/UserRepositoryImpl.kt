package com.vassev.meetingapp.data.remote.repository

import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.model.User
import com.vassev.meetingapp.domain.repository.UserRepository
import com.vassev.meetingapp.domain.requests.UpdateUserWithMeetingRequest
import com.vassev.meetingapp.domain.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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

    override suspend fun updateUsersWithMeeting(request: UpdateUserWithMeetingRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.put("http://${UserRepository.Endpoints.User.url}") {
                contentType(ContentType.Application.Json)
                body = request
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        TODO("Not yet implemented")
    }
}