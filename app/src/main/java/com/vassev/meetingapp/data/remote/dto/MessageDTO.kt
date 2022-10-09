package com.vassev.meetingapp.data.remote.dto

import com.vassev.meetingapp.domain.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
    val messageId: String,
    val text: String,
    val time: Long
) {
    fun fromDTOToEntity(): Message {
        return Message(
            text = text,
            time = time
        )
    }
}
