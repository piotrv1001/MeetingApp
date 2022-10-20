package com.vassev.meetingapp.data.remote.repository

import com.vassev.meetingapp.domain.repository.PlanRepository
import com.vassev.meetingapp.domain.requests.AddExceptionToRepeatedPlanRequest
import com.vassev.meetingapp.domain.requests.OneTimePlanRequest
import com.vassev.meetingapp.domain.requests.PlanRequest
import com.vassev.meetingapp.domain.requests.RepeatedPlanRequest
import com.vassev.meetingapp.domain.responses.PlanResponse
import com.vassev.meetingapp.domain.util.Resource
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class PlanRepositoryImpl(
    private val client: HttpClient
): PlanRepository {

    override suspend fun getPlansForUserOnDay(planRequest: PlanRequest): PlanResponse? {
        return try {
            client.get("http://${PlanRepository.Endpoints.Plan.url}") {
                contentType(ContentType.Application.Json)
                body = planRequest
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun insertOneTimePlan(oneTimePlanRequest: OneTimePlanRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.post("http://${PlanRepository.Endpoints.OneTimePlan.url}") {
                contentType(ContentType.Application.Json)
                body = oneTimePlanRequest
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun updateOneTimePlan(oneTimePlanRequest: OneTimePlanRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.put("http://${PlanRepository.Endpoints.OneTimePlan.url}") {
                contentType(ContentType.Application.Json)
                body = oneTimePlanRequest
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun insertRepeatedPlan(repeatedPlanRequest: RepeatedPlanRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.post("http://${PlanRepository.Endpoints.RepeatedPlan.url}") {
                contentType(ContentType.Application.Json)
                body = repeatedPlanRequest
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun updateRepeatedPlan(repeatedPlanRequest: RepeatedPlanRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.put("http://${PlanRepository.Endpoints.RepeatedPlan.url}") {
                contentType(ContentType.Application.Json)
                body = repeatedPlanRequest
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun addExceptionToRepeatedPlan(addExceptionToRepeatedPlanRequest: AddExceptionToRepeatedPlanRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.post("http://${PlanRepository.Endpoints.AddException.url}") {
                contentType(ContentType.Application.Json)
                body = addExceptionToRepeatedPlanRequest
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }
}