package com.vassev.meetingapp.presentation.calendar.components

import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.model.SpecificDay
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.calendar.CalendarEvent
import com.vassev.meetingapp.presentation.calendar.CalendarViewmodel
import com.vassev.meetingapp.presentation.util.Screen
import java.time.LocalDate

@Composable
fun CalendarScreen(
    viewModel: CalendarViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.calendarResults.collect { result ->
            when(result) {
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Error: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {}
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(factory = { CalendarView(it) }, update = {
            it.setOnDateChangeListener{ _, year, month, day ->
                val localDate = LocalDate.of(year, month + 1, day)
                val dayOfWeek = localDate.dayOfWeek.value
                val specificDay = SpecificDay(day, month + 1, year)
                viewModel.onEvent(CalendarEvent.SpecificDaySelected(specificDay, dayOfWeek))
            }
        })
    }
}