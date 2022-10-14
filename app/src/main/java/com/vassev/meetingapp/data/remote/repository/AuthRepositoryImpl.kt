package com.vassev.meetingapp.data.remote.repository

import android.content.SharedPreferences
import com.vassev.meetingapp.domain.repository.AuthRepository
import com.vassev.meetingapp.domain.requests.LoginRequest
import com.vassev.meetingapp.domain.requests.RegisterRequest
import com.vassev.meetingapp.domain.responses.TokenResponse
import com.vassev.meetingapp.domain.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val prefs: SharedPreferences
): AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Resource<Unit> {
        return try {
            val response: TokenResponse = client.post("http://${AuthRepository.Endpoints.User.url}/login") {
                contentType(ContentType.Application.Json)
                body = loginRequest
            }
            prefs.edit()
                .putString("jwt", response.token)
                .putString("userId", response.userId)
                .apply()
            Resource.Success()
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Resource<Unit> {
        return try {
            val response: HttpResponse = client.post("http://${AuthRepository.Endpoints.User.url}/register") {
                body = registerRequest
                contentType(ContentType.Application.Json)
            }
            if(response.status == HttpStatusCode.OK) {
                val loginRequest = LoginRequest(
                    email = registerRequest.email,
                    password = registerRequest.password
                )
                login(loginRequest)
            }
            else {
                Resource.Error("Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun authenticate(): Resource<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return Resource.Unauthorized()
            val response: HttpResponse = client.get("http://${AuthRepository.Endpoints.User.url}/authenticate") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            if(response.status == HttpStatusCode.OK) {
                Resource.Success()
            } else {
                Resource.Error("Http Error")
            }
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }
}