package com.vassev.meetingapp.presentation.calendar

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.SpecificDay
import com.vassev.meetingapp.domain.repository.PlanRepository
import com.vassev.meetingapp.domain.requests.PlanRequest
import com.vassev.meetingapp.domain.responses.PlanResponse
import com.vassev.meetingapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewmodel @Inject constructor(
    private val planRepository: PlanRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    private val resultChannel = Channel<Resource<Unit>>()
    val calendarResults = resultChannel.receiveAsFlow()

    private val userId = prefs.getString("userId", "ERROR") ?: ""

    fun onEvent(event: CalendarEvent) {
        when(event) {
            is CalendarEvent.SpecificDaySelected -> {
                loadPlansForSpecificDay(event.specificDay, event.dayOfWeek)
            }
        }
    }

    private fun loadPlansForSpecificDay(specificDay: SpecificDay, dayOfWeek: Int) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }
            val response: PlanResponse? = planRepository.getPlansForUserOnDay(
                PlanRequest(
                    specificDay = specificDay,
                    userId = userId,
                    dayOfWeek = dayOfWeek
                )
            )
            if(response == null) {
                resultChannel.send(Resource.Error("Error! Could not load plans"))
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }
            } else {
                val oneTimePlanList = response.oneTimePlan?.plans
                val repeatedPlanList = response.repeatedPlan?.plans
                val resultList: MutableList<Plan> = ArrayList()
                if(oneTimePlanList != null) {
                    resultList.addAll(oneTimePlanList)
                }
                if(repeatedPlanList != null) {
                    resultList.addAll(repeatedPlanList)
                }
                _state.update { currentState ->
                    currentState.copy(
                        plans = resultList,
                        isLoading = false
                    )
                }
            }
        }
    }
    
}