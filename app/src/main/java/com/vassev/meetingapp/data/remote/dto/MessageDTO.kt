package com.vassev.meetingapp.data.remote.dto

import com.vassev.meetingapp.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class MessageDTO(
    val messageId: String,
    val text: String,
    val timestamp: Long,
    val userId: String,
    val meetingId: String
) {
    fun fromDTOToEntity(): Message {
        val date = Date(timestamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)
        return Message(
            text = text,
            formattedTime = formattedDate,
            userId = userId,
            meetingId = meetingId
        )
    }
}
