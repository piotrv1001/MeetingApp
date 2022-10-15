package com.vassev.meetingapp.presentation.add_edit_meeting

import com.vassev.meetingapp.data.remote.dto.UserDTO

sealed class AddEditMeetingEvent {
    data class TitleChanged(val newTitle: String): AddEditMeetingEvent()
    data class HoursChanged(val newHours: String): AddEditMeetingEvent()
    data class MinutesChanged(val newMinutes: String): AddEditMeetingEvent()
    data class LocationChanged(val newLocation: String): AddEditMeetingEvent()
    data class SearchedUserChanged(val newSearchedUser: String): AddEditMeetingEvent()
    object SaveButtonClicked: AddEditMeetingEvent()
    object SearchUsersButtonClicked: AddEditMeetingEvent()
    object GoBackButtonClicked: AddEditMeetingEvent()
    data class UserChecked(val userDTO: UserDTO, val checked: Boolean): AddEditMeetingEvent()
}
