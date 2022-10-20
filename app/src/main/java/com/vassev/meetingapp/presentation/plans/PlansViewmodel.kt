package com.vassev.meetingapp.presentation.plans

import androidx.lifecycle.ViewModel
import com.vassev.meetingapp.domain.repository.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlansViewmodel @Inject constructor(
    private val planRepository: PlanRepository
) : ViewModel() {


}