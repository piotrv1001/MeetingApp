package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.domain.model.Plan
import java.util.*

interface PlanRepository {

    suspend fun getPlansForUserOnDay(userId: String, day: Date): List<Plan>

    suspend fun addPlan(userId: String)

    suspend fun updatePlan(userId: String)

    suspend fun deletePlan(userId: String)
}