package com.vassev.meetingapp.presentation.generate_time.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.util.DateUtil
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.generate_time.GenerateTimeEvent
import com.vassev.meetingapp.presentation.generate_time.GenerateTimeViewmodel
import com.vassev.meetingapp.presentation.util.Screen
import java.time.LocalDate

@Composable
fun GenerateTimeScreen(
    viewModel: GenerateTimeViewmodel = hiltViewModel(),
    navController: NavController,
    meetingId: String?
) {

    val state by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.saveResults.collect { result ->
            when(result) {
                is Resource.Success -> {
                    Toast.makeText(
                        context,
                        "Successfully saved meeting time!",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }
                is Resource.Error -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Error: ${result.message}"
                    )
                }
                else -> {}
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xfff5f5f5))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "How many weeks ahead?"
                    )
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.onEvent(GenerateTimeEvent.DecrementWeeks)},
                            modifier = Modifier
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Blue,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(
                                start = 10.dp,
                                end = 10.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease weeks ahead"
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = state.numberOfWeeks.toString()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { viewModel.onEvent(GenerateTimeEvent.IncrementWeeks)},
                            modifier = Modifier
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Blue,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(
                                start = 10.dp,
                                end = 10.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase weeks ahead"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "How many results?"
                    )
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Button(
                            onClick = { viewModel.onEvent(GenerateTimeEvent.DecrementResults)},
                            modifier = Modifier
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Blue,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(
                                start = 10.dp,
                                end = 10.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease results"
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = state.numberOfResults.toString()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { viewModel.onEvent(GenerateTimeEvent.IncrementResults)},
                            modifier = Modifier
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Blue,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(
                                start = 10.dp,
                                end = 10.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase results"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.onEvent(GenerateTimeEvent.ChooseMorning) }
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (state.preferredTime == 1) Color.Black else Color(0xfff5f5f5))
                            .padding(
                                horizontal = 8.dp,
                                vertical = 12.dp,
                            ),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "Morning",
                            tint = if(state.preferredTime == 1) Color.White else Color.Black
                        )
                        Text(
                            text = "Morning",
                            color = if(state.preferredTime == 1) Color.White else Color.Black
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.onEvent(GenerateTimeEvent.ChooseAfternoon) }
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (state.preferredTime == 2) Color.Black else Color(0xfff5f5f5))
                            .padding(
                                horizontal = 8.dp,
                                vertical = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = "Afternoon",
                            tint = if(state.preferredTime == 2) Color.White else Color.Black
                        )
                        Text(
                            text = "Afternoon",
                            color = if(state.preferredTime == 2) Color.White else Color.Black
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.onEvent(GenerateTimeEvent.ChooseEvening) }
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (state.preferredTime == 3) Color.Black else Color(0xfff5f5f5))
                            .padding(
                                horizontal = 8.dp,
                                vertical = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Default.ModeNight,
                            contentDescription = "Evening",
                            tint = if(state.preferredTime == 3) Color.White else Color.Black
                        )
                        Text(
                            text = "Evening",
                            color = if(state.preferredTime == 3) Color.White else Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    meetingId?.let {
                        GenerateTimeEvent.GenerateTimeButtonClicked(
                            it
                        )
                    }?.let { viewModel.onEvent(it) }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(CenterHorizontally)
            ) {
                Text(
                    text = "Generate meeting time"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    meetingId?.let {
                        GenerateTimeEvent.SaveTimeButtonClicked(
                            it
                        )
                    }?.let { viewModel.onEvent(it) }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(CenterHorizontally),
                enabled = state.chosenTime != null
            ) {
                Text(
                    text = "Save meeting time"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            if(state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(
                        items = state.generatedTimes
                    ) { index, generatedTime ->
                        val dayOfWeek = LocalDate.of(generatedTime.specificDay.year, generatedTime.specificDay.month, generatedTime.specificDay.day).dayOfWeek.value
                        val dayOfWeekName = DateUtil.getDayOfWeekName(dayOfWeek)
                        val day = generatedTime.specificDay.day
                        val month = generatedTime.specificDay.month
                        val year = generatedTime.specificDay.year
                        val monthName = DateUtil.getMonthName(month)
                        val fromHour = generatedTime.plan.fromHour
                        val fromMinute = generatedTime.plan.fromMinute
                        val toHour = generatedTime.plan.toHour
                        val toMinute = generatedTime.plan.toMinute
                        val formattedPlan = DateUtil.getFormattedPlan(
                            fromHour = fromHour,
                            fromMinute = fromMinute,
                            toHour = toHour,
                            toMinute = toMinute
                        )
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(if(state.chosenTime == generatedTime) Color.DarkGray else Color(0xFFF5F5F5))
                                .padding(8.dp)
                                .clickable {
                                    viewModel.onEvent(
                                        GenerateTimeEvent.ChooseMeetingTime(
                                            generatedTime
                                        )
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Box(
                                modifier = Modifier
                                    .background(Color.DarkGray, shape = CircleShape)
                                    .layout { measurable, constraints ->
                                        val placeable = measurable.measure(constraints)
                                        val minPadding = placeable.height / 4
                                        val width =
                                            maxOf(placeable.width + minPadding, placeable.height)
                                        layout(width, placeable.height) {
                                            placeable.place((width - placeable.width) / 2, 0)
                                        }
                                    },
                                contentAlignment = Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "$dayOfWeekName, $day $monthName $year, $formattedPlan",
                                color = if(state.chosenTime == generatedTime) Color.White else Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}