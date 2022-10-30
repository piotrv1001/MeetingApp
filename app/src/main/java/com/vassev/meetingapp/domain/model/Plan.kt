package com.vassev.meetingapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val fromHour: Int,
    val toHour: Int,
    val fromMinute: Int,
    val toMinute: Int,
    val name: String = ""
) {
    fun toPlanWithType(repeat: Boolean): PlanWithType {
        return PlanWithType(
            fromHour = fromHour,
            fromMinute = fromMinute,
            toHour = toHour,
            toMinute = toMinute,
            name = name,
            repeat = repeat
        )
    }
}
