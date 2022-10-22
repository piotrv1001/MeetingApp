package com.vassev.meetingapp.domain.repository

import com.vassev.meetingapp.domain.requests.AddExceptionToRepeatedPlanRequest
import com.vassev.meetingapp.domain.requests.OneTimePlanRequest
import com.vassev.meetingapp.domain.requests.PlanRequest
import com.vassev.meetingapp.domain.requests.RepeatedPlanRequest
import com.vassev.meetingapp.domain.responses.PlanResponse
import com.vassev.meetingapp.domain.util.Constants
import com.vassev.meetingapp.domain.util.Resource

interface PlanRepository {

    suspend fun getPlansForUserOnDay(planRequest: PlanRequest): PlanResponse?

    suspend fun insertOneTimePlan(oneTimePlanRequest: OneTimePlanRequest): Resource<Unit>

    suspend fun updateOneTimePlan(oneTimePlanRequest: OneTimePlanRequest): Resource<Unit>

    suspend fun insertRepeatedPlan(repeatedPlanRequest: RepeatedPlanRequest): Resource<Unit>

    suspend fun updateRepeatedPlan(repeatedPlanRequest: RepeatedPlanRequest): Resource<Unit>

    suspend fun addExceptionToRepeatedPlan(addExceptionToRepeatedPlanRequest: AddExceptionToRepeatedPlanRequest): Resource<Unit>

    suspend fun deletePlanFromOneTimePlan(oneTimePlanRequest: OneTimePlanRequest): Resource<Unit>

    suspend fun deleteOneTimePlan(oneTimePlanRequest: OneTimePlanRequest): Resource<Unit>

    suspend fun deleteRepeatedPlan(repeatedPlanRequest: RepeatedPlanRequest): Resource<Unit>

    sealed class Endpoints(val url: String) {
        object Plan: Endpoints("${Constants.BASE_URL}/plan")
        object OneTimePlan: Endpoints("${Constants.BASE_URL}/plan/oneTimePlan")
        object RepeatedPlan: Endpoints("${Constants.BASE_URL}/plan/repeatedPlan")
        object AddException: Endpoints("${Constants.BASE_URL}/plan/repeatedPlan/except")
    }
}