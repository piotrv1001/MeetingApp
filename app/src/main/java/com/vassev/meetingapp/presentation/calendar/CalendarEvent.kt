package com.vassev.meetingapp.presentation.calendar

import com.vassev.meetingapp.domain.model.SpecificDay

sealed class CalendarEvent {
    data class SpecificDaySelected(val specificDay: SpecificDay, val dayOfWeek: Int): CalendarEvent()
}
