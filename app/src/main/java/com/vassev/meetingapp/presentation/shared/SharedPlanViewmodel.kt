package com.vassev.meetingapp.presentation.shared

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassev.meetingapp.domain.model.Plan
import com.vassev.meetingapp.domain.model.PlanWithType
import com.vassev.meetingapp.domain.model.SpecificDay
import com.vassev.meetingapp.domain.repository.MeetingRepository
import com.vassev.meetingapp.domain.repository.PlanRepository
import com.vassev.meetingapp.domain.repository.UserRepository
import com.vassev.meetingapp.domain.requests.AddExceptionToRepeatedPlanRequest
import com.vassev.meetingapp.domain.requests.OneTimePlanRequest
import com.vassev.meetingapp.domain.requests.PlanRequest
import com.vassev.meetingapp.domain.requests.RepeatedPlanRequest
import com.vassev.meetingapp.domain.responses.PlanResponse
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.home.HomeEvent
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
    private val prefs: SharedPreferences,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SharedPlanState())
    val state = _state.asStateFlow()

    var dayOfWeek: Int = 0

    private val resultChannel = Channel<Resource<Unit>>()
    val sharedPlanResults = resultChannel.receiveAsFlow()

    private var userId = prefs.getString("userId", "ERROR") ?: ""

    private val dayHashMap = hashMapOf<SpecificDay, List<PlanWithType>>()

    init {
        initializeState()
    }

    fun onEvent(event: SharedPlanEvent) {
        when(event) {
            is SharedPlanEvent.ReloadData -> {
                reloadUserId()
                dayHashMap.clear()
                loadPlansForSpecificDay(event.specificDay)
            }
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
                        currentPLan = event.plan
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
            is SharedPlanEvent.DeletePlanClicked -> {
                deleteCurrentPlan()
            }
            is SharedPlanEvent.LeaveMeetingButtonClicked -> {
                leaveMeeting()
            }
        }
    }

    private fun reloadUserId() {
        userId = prefs.getString("userId", "ERROR") ?: ""
    }

    private fun leaveMeeting() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
           meetingRepository.leaveMeeting(
                meetingId = state.value.currentPLan.meetingId,
                userId = userId
            )
            val resultTwo = planRepository.deletePlanFromOneTimePlan(
                OneTimePlanRequest(
                    specificDay = state.value.specificDay,
                    userId = userId,
                    plans = listOf(
                        Plan(
                            fromHour = state.value.currentPLan.fromHour,
                            fromMinute = state.value.currentPLan.fromMinute,
                            toHour = state.value.currentPLan.toHour,
                            toMinute = state.value.currentPLan.toMinute,
                            name = state.value.currentPLan.name,
                            meetingId = state.value.currentPLan.meetingId
                        )
                    )
                )
            )
            resultChannel.send(resultTwo)
            _state.update { currentState ->
                currentState.copy(
                    showDialog = false
                )
            }
            if(dayHashMap.containsKey(state.value.specificDay)) {
                dayHashMap.remove(state.value.specificDay)
            }
            if(dayHashMap.contains(state.value.specificDay)) {
                _state.update { currentState ->
                    currentState.copy(
                        plans = dayHashMap[state.value.specificDay] ?: emptyList(),
                    )
                }
            } else {
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

    private fun validateInput(): Boolean {
        return if(state.value.fromHour.isNotEmpty()
            && state.value.fromMinute.isNotEmpty()
            && state.value.toHour.isNotEmpty()
            && state.value.toMinute.isNotEmpty()
            && state.value.fromHour.toInt() in 0..23
            && state.value.fromMinute.toInt() in 0..59
            && state.value.toHour.toInt() in 0..23
            && state.value.toMinute.toInt() in 0..59
            && state.value.fromHour.toInt() < state.value.toHour.toInt()) {
            true
        } else if(state.value.fromHour == state.value.toHour) {
            state.value.fromMinute.toInt() < state.value.toMinute.toInt()
        } else {
            false
        }
    }

    private fun checkIfPlanAlreadyExists(): Boolean {
        val newPlan = Plan(
            fromHour = state.value.fromHour.toInt(),
            fromMinute = state.value.fromMinute.toInt(),
            toHour = state.value.toHour.toInt(),
            toMinute = state.value.toMinute.toInt()
        )
        return (state.value.plans.find{ newPlan.isWithinAnotherPlan(it.toPlan()) }) != null
    }

    private fun addPlan() {
        viewModelScope.launch {
            if(!validateInput()) {
                resultChannel.send(Resource.Error("Make sure the numbers are correct"))
                return@launch
            } else if(checkIfPlanAlreadyExists()) {
                resultChannel.send(Resource.Error("Plan unnecessary"))
                return@launch
            }
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }
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
                val response: PlanResponse? = planRepository.getPlansForUserOnDay(
                    PlanRequest(
                        specificDay = state.value.specificDay,
                        userId = userId,
                        dayOfWeek = dayOfWeek
                    )
                )
                if(response == null) {
                    resultChannel.send(Resource.Error("Error! Could not load plans"))
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
                    toMinute = "",
                    isRepeatChecked = false,
                    isLoading = false
                )
            }
        }
    }

    private fun deleteCurrentPlan() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val currentPlan = state.value.currentPLan
            val removeEveryWeek = state.value.removeEveryWeek
            if(currentPlan.repeat) {
                if(removeEveryWeek) {
                    val response = planRepository.deleteRepeatedPlan(
                        RepeatedPlanRequest(
                            userId = userId,
                            dayOfWeek = dayOfWeek
                        )
                    )
                    resultChannel.send(response)
                    dayHashMap.clear()
                } else {
                    val response = planRepository.addExceptionToRepeatedPlan(
                        AddExceptionToRepeatedPlanRequest(
                            userId = userId,
                            dayOfWeek = dayOfWeek,
                            specificDay = state.value.specificDay
                        )
                    )
                    resultChannel.send(response)
                    if(dayHashMap.containsKey(state.value.specificDay)) {
                        dayHashMap.remove(state.value.specificDay)
                    }
                }
            } else {
                val response = planRepository.deletePlanFromOneTimePlan(
                    OneTimePlanRequest(
                        specificDay = state.value.specificDay,
                        userId = userId,
                        plans = listOf(state.value.currentPLan.toPlan())
                    )
                )
                resultChannel.send(response)
                if(dayHashMap.containsKey(state.value.specificDay)) {
                    dayHashMap.remove(state.value.specificDay)
                }
            }
            _state.update { currentState ->
                currentState.copy(
                    showDialog = false,
                )
            }
            if(dayHashMap.contains(state.value.specificDay)) {
                _state.update { currentState ->
                    currentState.copy(
                        plans = dayHashMap[state.value.specificDay] ?: emptyList(),
                    )
                }
            } else {
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