package com.vassev.meetingapp.data.remote.dto

import com.vassev.meetingapp.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val userId: String,
    val email: String,
    val password: String,
    val name: String,
    val location: String,
    val meetings: List<String> = emptyList()
) {
    fun fromDTOToEntity(): User {
        return User(
            email = email,
            password = password,
            name = name,
            location = location,
            meetings = meetings
        )
    }
}
