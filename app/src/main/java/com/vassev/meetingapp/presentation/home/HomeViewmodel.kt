package com.vassev.meetingapp.presentation.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.model.Meeting
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.repository.UserRepository
import com.vassev.meetingapp.domain.requests.MeetingsForUserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val prefs: SharedPreferences,
    private val meetingRepository: MeetingRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _meetings = MutableStateFlow<List<MeetingDTO>>(emptyList())
    val meetings = _meetings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val userId = prefs.getString("userId", "ERROR") ?: ""
    var currentUser: UserDTO? = null

//    init {
//        loadMeetingsForUser(userId)
//    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.ReloadData -> {
                loadMeetingsForUser(userId)
            }
        }
    }

    private fun loadMeetingsForUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            currentUser = userRepository.getUserById(userId)
            _meetings.value = meetingRepository.getAllMeetingsForUser(
                MeetingsForUserRequest(
                    meetingIds = currentUser?.meetings ?: emptyList()
                )
            )
            _isLoading.value = false
        }
    }
}