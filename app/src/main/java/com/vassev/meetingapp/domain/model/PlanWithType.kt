package com.vassev.meetingapp.domain.model

data class PlanWithType(
    val fromHour: Int,
    val toHour: Int,
    val fromMinute: Int,
    val toMinute: Int,
    val repeat: Boolean
) {
    fun toPlan(): Plan{
        return Plan(
            fromHour = fromHour,
            fromMinute = fromMinute,
            toMinute = toMinute,
            toHour = toHour
        )
    }
}
