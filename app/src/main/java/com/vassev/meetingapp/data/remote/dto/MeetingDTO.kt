package com.vassev.meetingapp.data.remote.dto

import com.vassev.meetingapp.domain.model.Meeting
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDTO(
    val meetingId: String,
    val name: String,
    val duration: Int,
    val date: Long,
    val location: String,
    val users: List<String> = emptyList()
) {
    fun fromDTOToEntity(): Meeting {
        return Meeting(
            name = name,
            duration = duration,
            date = date,
            location = location,
            users = users
        )
    }
}
