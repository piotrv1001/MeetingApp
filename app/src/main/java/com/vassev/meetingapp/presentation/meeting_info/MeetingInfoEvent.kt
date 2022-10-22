package com.vassev.meetingapp.presentation.meeting_info

sealed class MeetingInfoEvent {
    data class LoadMeetingData(val meetingId: String): MeetingInfoEvent()
    object GenerateMeetingTimeButtonClicked: MeetingInfoEvent()
}
