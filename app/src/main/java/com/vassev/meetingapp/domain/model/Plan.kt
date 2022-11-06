package com.vassev.meetingapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val fromHour: Int,
    val toHour: Int,
    val fromMinute: Int,
    val toMinute: Int,
    val name: String = "",
    val meetingId: String = ""
) {
    fun toPlanWithType(repeat: Boolean): PlanWithType {
        return PlanWithType(
            fromHour = fromHour,
            fromMinute = fromMinute,
            toHour = toHour,
            toMinute = toMinute,
            name = name,
            meetingId = meetingId,
            repeat = repeat
        )
    }

    fun startTime(): HalfPlan {
        return HalfPlan(
            hour = this.fromHour,
            minute = this.fromMinute
        )
    }

    fun endTime(): HalfPlan {
        return HalfPlan(
            hour = this.toHour,
            minute = this.toMinute
        )
    }

    fun isWithinAnotherPlan(anotherPlan: Plan): Boolean {
        return this.startTime() >= anotherPlan.startTime() && this.endTime() <= anotherPlan.endTime()
    }
}
