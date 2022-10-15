package com.vassev.meetingapp.presentation.add_edit_meeting

sealed class AddEditMeetingEvent {
    data class TitleChanged(val newTitle: String): AddEditMeetingEvent()
    data class HoursChanged(val newHours: String): AddEditMeetingEvent()
    data class MinutesChanged(val newMinutes: String): AddEditMeetingEvent()
    data class LocationChanged(val newLocation: String): AddEditMeetingEvent()
    object SaveButtonClicked: AddEditMeetingEvent()
}
