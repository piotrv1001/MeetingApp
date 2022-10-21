package com.vassev.meetingapp.presentation.shared

import com.vassev.meetingapp.domain.model.SpecificDay

sealed class SharedPlanEvent {
    data class SpecificDaySelected(val specificDay: SpecificDay): SharedPlanEvent()
    data class FromHourChanged(val newFromHour: String): SharedPlanEvent()
    data class FromMinuteChanged(val newFromMinute: String): SharedPlanEvent()
    data class ToHourChanged(val newToHour: String): SharedPlanEvent()
    data class ToMinuteChanged(val newToMinute: String): SharedPlanEvent()
    data class RepeatCheckChanged(val newRepeatCheck: Boolean): SharedPlanEvent()
    object AddPlanButtonClicked: SharedPlanEvent()
    data class  RemovePlanButtonClicked(val repeat: Boolean): SharedPlanEvent()
    object CloseDialogClicked: SharedPlanEvent()
    object RemoveOnceRadioButtonClicked: SharedPlanEvent()
    object RemoveALlRadioButtonClicked: SharedPlanEvent()
}
