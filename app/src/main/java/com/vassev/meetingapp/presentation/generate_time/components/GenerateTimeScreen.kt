package com.vassev.meetingapp.presentation.generate_time.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.util.DateUtil
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.add_edit_meeting.AddEditMeetingEvent
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
                        message = "Error: ${result.message}",
                        duration = SnackbarDuration.Short
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back",
                        tint = MaterialTheme.colors.primary
                    )
                }
                TextButton(
                    onClick = {
                    meetingId?.let {
                        GenerateTimeEvent.SaveTimeButtonClicked(
                            it
                        )
                    }?.let { viewModel.onEvent(it) }
                },
                enabled = state.chosenTime != null && !state.isLoading
                ) {
                    Text(
                        text = "Save",
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                shape = RoundedCornerShape(10.dp),
                elevation = 18.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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
                                enabled = state.numberOfWeeks > 1,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                                shape = RoundedCornerShape(10.dp),
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
                                enabled = state.numberOfWeeks < 9,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                                shape = RoundedCornerShape(10.dp),
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
                    Text(
                        text = "How many results?"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Checkbox(
                                checked = state.showAllResults,
                                onCheckedChange = { checked ->
                                    viewModel.onEvent(GenerateTimeEvent.ShowAllResultsCheckboxClicked(checked))
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = Color.Gray,
                                    checkedColor = Color.Gray,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "All possible results",
                            )
                        }
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Button(
                                onClick = { viewModel.onEvent(GenerateTimeEvent.DecrementResults)},
                                enabled = !state.showAllResults && state.numberOfResults > 1,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                                shape = RoundedCornerShape(10.dp),
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
                                enabled = !state.showAllResults && state.numberOfResults < 9,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                                shape = RoundedCornerShape(10.dp),
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
                }
            }
            Spacer(modifier = Modifier.height(64.dp))
            Button(
                onClick = {
                    meetingId?.let {
                        GenerateTimeEvent.GenerateTimeButtonClicked(
                            it
                        )
                    }?.let { viewModel.onEvent(it) }
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            ) {
                Text(
                    text = "Generate"
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
            if(state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if(state.isAlreadyGenerated && state.generatedTimes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        Text(
                            text = "No results found",
                            color = MaterialTheme.colors.onError,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
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
                            Card(
                                modifier = Modifier
                                    .clickable {
                                        viewModel.onEvent(
                                            GenerateTimeEvent.ChooseMeetingTime(
                                                generatedTime
                                            )
                                        )
                                    },
                                shape = RoundedCornerShape(10.dp),
                                elevation = 18.dp,
                                border = if (state.chosenTime == generatedTime) BorderStroke(1.dp, MaterialTheme.colors.primary) else null
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Filled.CalendarToday,
                                            contentDescription = "Calendar",
                                            tint = MaterialTheme.colors.onBackground
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = "$dayOfWeekName, $day $monthName $year"
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row {
                                        Icon(
                                            imageVector = Icons.Filled.Timer,
                                            contentDescription = "Time",
                                            tint = MaterialTheme.colors.onBackground
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = formattedPlan
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}