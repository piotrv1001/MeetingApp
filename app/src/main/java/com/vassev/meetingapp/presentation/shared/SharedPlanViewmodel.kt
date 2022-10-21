package com.vassev.meetingapp.presentation.shared

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.PlanWithType
import com.vassev.meetingapp.domain.model.SpecificDay
import com.vassev.meetingapp.domain.repository.PlanRepository
import com.vassev.meetingapp.domain.requests.OneTimePlanRequest
import com.vassev.meetingapp.domain.requests.PlanRequest
import com.vassev.meetingapp.domain.requests.RepeatedPlanRequest
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
class SharedPlanViewmodel @Inject constructor(
    private val planRepository: PlanRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SharedPlanState())
    val state = _state.asStateFlow()

    val firstTwoPlans = state.map { state ->
        state.plans.sortedBy { it.fromHour }.take(2)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    var dayOfWeek: Int = 0

    private val resultChannel = Channel<Resource<Unit>>()
    val sharedPlanResults = resultChannel.receiveAsFlow()

    private val userId = prefs.getString("userId", "ERROR") ?: ""

    private val dayHashMap = hashMapOf<SpecificDay, List<PlanWithType>>()

    init {
        initializeState()
    }

    fun onEvent(event: SharedPlanEvent) {
        when(event) {
            is SharedPlanEvent.SpecificDaySelected -> {
                loadPlansForSpecificDay(event.specificDay)
            }
            is SharedPlanEvent.FromHourChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        fromHour = event.newFromHour
                    )
                }
            }
            is SharedPlanEvent.FromMinuteChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        fromMinute = event.newFromMinute
                    )
                }
            }
            is SharedPlanEvent.ToHourChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        toHour = event.newToHour
                    )
                }
            }
            is SharedPlanEvent.ToMinuteChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        toMinute = event.newToMinute
                    )
                }
            }
            is SharedPlanEvent.RepeatCheckChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        isRepeatChecked = event.newRepeatCheck
                    )
                }
            }
            is SharedPlanEvent.AddPlanButtonClicked -> {
                addPlan()
            }
            is SharedPlanEvent.RemovePlanButtonClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        showDialog = true,
                        removingRepeatedPlan = event.repeat
                    )
                }
            }
            is SharedPlanEvent.CloseDialogClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        showDialog = false
                    )
                }
            }
            is SharedPlanEvent.RemoveOnceRadioButtonClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        removeEveryWeek = false
                    )
                }
            }
            is SharedPlanEvent.RemoveALlRadioButtonClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        removeEveryWeek = true
                    )
                }
            }
        }
    }

    private fun loadPlansForSpecificDay(specificDay: SpecificDay) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    specificDay = specificDay,
                )
            }
            updateDayOfWeek()
            if(dayHashMap.contains(specificDay)) {
                _state.update { currentState ->
                    currentState.copy(
                        plans = dayHashMap[specificDay] ?: emptyList(),
                    )
                }
            } else {
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
                    val resultList: MutableList<PlanWithType> = ArrayList()
                    if(oneTimePlanList != null) {
                        resultList.addAll(oneTimePlanList.map { it.toPlanWithType(false) })
                    }
                    if(repeatedPlanList != null) {
                        resultList.addAll(repeatedPlanList.map { it.toPlanWithType(true) })
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

    private fun addPlan() {
        viewModelScope.launch {
            if(state.value.isRepeatChecked) {
                val response = planRepository.insertRepeatedPlan(
                    RepeatedPlanRequest(
                        dayOfWeek = dayOfWeek,
                        userId = userId,
                        plans = listOf(
                            Plan(
                                fromHour = state.value.fromHour.toInt(),
                                fromMinute = state.value.fromMinute.toInt(),
                                toHour = state.value.toHour.toInt(),
                                toMinute = state.value.toMinute.toInt()
                        ))
                    )
                )
                resultChannel.send(response)
                dayHashMap.clear()
            } else {
                val response = planRepository.insertOneTimePlan(
                    OneTimePlanRequest(
                        userId = userId,
                        specificDay = state.value.specificDay,
                        plans = listOf(
                            Plan(
                                fromHour = state.value.fromHour.toInt(),
                                fromMinute = state.value.fromMinute.toInt(),
                                toHour = state.value.toHour.toInt(),
                                toMinute = state.value.toMinute.toInt()
                            ))
                    )
                )
                resultChannel.send(response)
                if(dayHashMap.containsKey(state.value.specificDay)) {
                    dayHashMap.remove(state.value.specificDay)
                }
            }
            if(dayHashMap.contains(state.value.specificDay)) {
                _state.update { currentState ->
                    currentState.copy(
                        plans = dayHashMap[state.value.specificDay] ?: emptyList(),
                    )
                }
            } else {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = true,
                    )
                }
                val response: PlanResponse? = planRepository.getPlansForUserOnDay(
                    PlanRequest(
                        specificDay = state.value.specificDay,
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
                    val resultList: MutableList<PlanWithType> = ArrayList()
                    if(oneTimePlanList != null) {
                        resultList.addAll(oneTimePlanList.map { it.toPlanWithType(false) })
                    }
                    if(repeatedPlanList != null) {
                        resultList.addAll(repeatedPlanList.map { it.toPlanWithType(true) })
                    }
                    _state.update { currentState ->
                        currentState.copy(
                            plans = resultList,
                            isLoading = false,
                        )
                    }
                    dayHashMap[state.value.specificDay] = resultList
                }
            }
            _state.update { currentState ->
                currentState.copy(
                    fromHour = "",
                    fromMinute = "",
                    toHour = "",
                    toMinute = ""
                )
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
        dayOfWeek = today.dayOfWeek.value
        _state.update { currentState ->
            currentState.copy(
                specificDay = specificDay
            )
        }
    }

    private fun updateDayOfWeek() {
        dayOfWeek = LocalDate.of(state.value.specificDay.year, state.value.specificDay.month, state.value.specificDay.day).dayOfWeek.value
    }
    
}