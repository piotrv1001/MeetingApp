package com.vassev.meetingapp.presentation.meeting_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.data.remote.dto.MeetingDTO
import com.vassev.meetingapp.domain.model.Meeting
import com.vassev.meetingapp.domain.util.DateUtil
import com.vassev.meetingapp.presentation.meeting_info.MeetingInfoEvent
import com.vassev.meetingapp.presentation.meeting_info.MeetingInfoViewmodel
import com.vassev.meetingapp.presentation.util.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

@Composable
fun MeetingInfoScreen(
    viewModel: MeetingInfoViewmodel = hiltViewModel(),
    navController: NavController,
    meetingId: String?
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(true) {
        if(meetingId != null) {
            viewModel.onEvent(MeetingInfoEvent.LoadMeetingData(meetingId))
        }
    }
    if(state.isLoadingMeetingInfo) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back",
                        tint = Color.Blue
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                state.meetingDTO?.name?.let {
                    Text(
                        text = it,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xfff5f5f5))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        state.meetingDTO?.location?.let {
                            Text(
                                text = it,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row{
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "People"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = state.meetingDTO?.users?.size.toString() + " members",
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.Timelapse,
                            contentDescription = "Duration"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        val hours = state.meetingDTO?.duration?.div(60)
                        val minutes = state.meetingDTO?.duration?.rem(60)
                        if(minutes == 0) {
                            Text(
                                text = hours.toString() + " h",
                                fontSize = 16.sp
                            )
                        } else {
                            Text(
                                text = hours.toString() + " h " + minutes.toString() + " min",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Button(
                    onClick = { navController.navigate(Screen.AddEditMeetingScreen.route + "?meetingId=$meetingId") },
                    modifier = Modifier
                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 18.dp
                    ),
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.navigate(Screen.ChatScreen.route + "/$meetingId") }) {
                Text(
                    text = "Join chat room"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { viewModel.onEvent(MeetingInfoEvent.GenerateMeetingTimeButtonClicked) }) {
                Text(
                    text = "Generate meeting time"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            if(state.isLoadingGeneratedTime) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
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
                                .background(Color(0xFFF5F5F5))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Box(
                                modifier = Modifier
                                    .background(Color.DarkGray, shape = CircleShape)
                                    .layout { measurable, constraints ->
                                        val placeable = measurable.measure(constraints)
                                        val minPadding = placeable.height / 4
                                        val width = maxOf(placeable.width + minPadding, placeable.height)
                                        layout(width, placeable.height) {
                                            placeable.place((width - placeable.width) / 2, 0)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "$dayOfWeekName, $day $monthName $year, $formattedPlan"
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}