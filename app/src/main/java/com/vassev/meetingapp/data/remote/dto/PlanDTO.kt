package com.vassev.meetingapp.data.remote.dto

import com.vassev.meetingapp.domain.model.Plan
import kotlinx.serialization.Serializable

@Serializable
data class PlanDTO(
    val planId: String,
    val fromDate: Long,
    val toDate: Long
) {
    fun fromDTOToEntity(): Plan {
        return Plan(
            fromDate = fromDate,
            toDate = toDate
        )
    }
}
