package com.vassev.meetingapp.presentation.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val prefs: SharedPreferences
) : ViewModel() {

    val userId = prefs.getString("userId", "ERROR")
}