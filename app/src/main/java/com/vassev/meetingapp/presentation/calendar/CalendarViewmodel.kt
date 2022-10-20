package com.vassev.meetingapp.presentation.calendar

import android.content.SharedPreferences
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CalendarViewmodel @Inject constructor(
    private val planRepository: PlanRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    val firstTwoPlans = state.map { state ->
        state.plans.sortedBy { it.fromHour }.take(2)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val resultChannel = Channel<Resource<Unit>>()
    val calendarResults = resultChannel.receiveAsFlow()

    private val userId = prefs.getString("userId", "ERROR") ?: ""

    private val dayHashMap = hashMapOf<SpecificDay, List<Plan>>()

    init {
        initializeState()
    }

    fun onEvent(event: CalendarEvent) {
        when(event) {
            is CalendarEvent.SpecificDaySelected -> {
                loadPlansForSpecificDay(event.specificDay, event.dayOfWeek)
            }
        }
    }

    private fun loadPlansForSpecificDay(specificDay: SpecificDay, dayOfWeek: Int) {
        viewModelScope.launch {
            if(dayHashMap.contains(specificDay)) {
                _state.update { currentState ->
                    currentState.copy(
                        plans = dayHashMap[specificDay] ?: emptyList()
                    )
                }
            } else {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = true,
                        specificDay = specificDay
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
                            isLoading = false,
                        )
                    }
                    dayHashMap[specificDay] = resultList
                }
            }
        }
    }

    private fun initializeState() {
        val today = LocalDate.now()
        val specificDay = SpecificDay(
            day = today.dayOfMonth,
            month = today.monthValue,
            year = today.year
        )
        val dayOfWeek = today.dayOfWeek.value
        _state.update { currentState ->
            currentState.copy(
                specificDay = specificDay
            )
        }
        loadPlansForSpecificDay(specificDay, dayOfWeek)
    }
    
}