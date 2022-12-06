package com.vassev.meetingapp.presentation.meeting_info.components

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.presentation.meeting_info.MeetingInfoEvent
import com.vassev.meetingapp.presentation.meeting_info.MeetingInfoViewmodel
import com.vassev.meetingapp.presentation.util.Screen

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
                .fillMaxSize(),
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
                        tint = MaterialTheme.colors.primary
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
            Card(
                shape = RoundedCornerShape(10.dp),
                elevation = 18.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.navigate(Screen.ChatScreen.route + "/$meetingId") }) {
                Text(
                    text = "Join chat room"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate(Screen.GenerateTimeScreen.route + "/$meetingId") }
            ) {
                Text(
                    text = "Generate meeting time"
                )
            }
        }
    }
}