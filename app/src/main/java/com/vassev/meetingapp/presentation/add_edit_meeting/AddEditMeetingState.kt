package com.vassev.meetingapp.presentation.add_edit_meeting

import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.model.User

data class AddEditMeetingState(
    val isLoading: Boolean = false,
    val showSearchUsers: Boolean = false,
    val title: String = "",
    val hours: String = "",
    val minutes: String = "",
    val location: String = "",
    val searchedUser: String = "",
    val memberHashMap: HashMap<UserDTO, Boolean> = hashMapOf()
)