package com.vassev.meetingapp.presentation.calendar.components

import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.model.SpecificDay
import com.vassev.meetingapp.domain.util.DateUtil
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
    val firstTwoPlans by viewModel.firstTwoPlans.collectAsState()
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(factory = { CalendarView(it) }, update = {
            it.setOnDateChangeListener{ _, year, month, day ->
                val localDate = LocalDate.of(year, month + 1, day)
                val dayOfWeek = localDate.dayOfWeek.value
                val specificDay = SpecificDay(day, month + 1, year)
                viewModel.onEvent(CalendarEvent.SpecificDaySelected(specificDay, dayOfWeek))
            }
        })
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.height(150.dp).fillMaxWidth(0.9f),
            shape = RoundedCornerShape(10.dp),
            elevation = 18.dp
        ) {
            if(state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = DateUtil.getMonthName(state.specificDay.month),
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.specificDay.day.toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .width(1.dp),
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if(firstTwoPlans.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF90EE90))
                                    .padding(
                                        top = 4.dp,
                                        bottom = 4.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    )
                            ) {
                                val firstPlan = state.plans[0]
                                Text(
                                    text = DateUtil.getFormattedPlan(firstPlan.fromHour, firstPlan.fromMinute, firstPlan.toHour, firstPlan.toMinute),
                                    color = Color(0xFF06A94D),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        if(firstTwoPlans.size >= 2) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF90EE90))
                                    .padding(
                                        top = 4.dp,
                                        bottom = 4.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    )
                            ) {
                                val secondPlan = state.plans[1]
                                Text(
                                    text = DateUtil.getFormattedPlan(secondPlan.fromHour, secondPlan.fromMinute, secondPlan.toHour, secondPlan.toMinute),
                                    color = Color(0xFF06A94D),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}