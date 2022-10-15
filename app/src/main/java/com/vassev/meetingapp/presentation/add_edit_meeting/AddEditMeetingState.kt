package com.vassev.meetingapp.presentation.add_edit_meeting

import com.vassev.meetingapp.domain.model.User

data class AddEditMeetingState(
    val isLoading: Boolean = false,
    val title: String = "",
    val hours: String = "",
    val minutes: String = "",
    val location: String = "",
    val members: List<User> = emptyList()
)