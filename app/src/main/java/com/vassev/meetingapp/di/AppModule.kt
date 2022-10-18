package com.vassev.meetingapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.vassev.meetingapp.data.remote.repository.AuthRepositoryImpl
import com.vassev.meetingapp.data.remote.repository.MeetingRepositoryImpl
import com.vassev.meetingapp.data.remote.repository.MessageRepositoryImpl
import com.vassev.meetingapp.data.remote.repository.UserRepositoryImpl
import com.vassev.meetingapp.data.remote.service.WebSocketServiceImpl
import com.vassev.meetingapp.domain.repository.AuthRepository
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.repository.MessageRepository
import com.vassev.meetingapp.domain.repository.UserRepository
import com.vassev.meetingapp.domain.service.WebSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(client: HttpClient, prefs: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(client, prefs)
    }

    @Provides
    @Singleton
    fun provideMeetingRepository(client: HttpClient): MeetingRepository {
        return MeetingRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideUserRepository(client: HttpClient): UserRepository {
        return UserRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideWebSocketService(client: HttpClient): WebSocketService {
        return WebSocketServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(client: HttpClient): MessageRepository {
        return MessageRepositoryImpl(client)
    }

}