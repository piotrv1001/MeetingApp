package com.vassev.meetingapp.presentation.add_edit_meeting.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.add_edit_meeting.AddEditMeetingEvent
import com.vassev.meetingapp.presentation.add_edit_meeting.AddEditMeetingViewmodel
import com.vassev.meetingapp.presentation.register.RegisterEvent

@Composable
fun AddEditMeetingScreen(
    viewModel: AddEditMeetingViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.meetingResults.collect { result ->
            when(result) {
                is Resource.Success -> {
                    Toast.makeText(
                        context,
                        "Successfully saved meeting!",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }
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
    if(state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if(!state.showSearchUsers){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = state.title,
                onValueChange = {
                    viewModel.onEvent(AddEditMeetingEvent.TitleChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Title")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Filled.Timelapse,
                    contentDescription = "Meeting duration"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Duration")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    value = state.hours,
                    onValueChange = {
                        viewModel.onEvent(AddEditMeetingEvent.HoursChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Gray)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "h")
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    value = state.minutes,
                    onValueChange = {
                        viewModel.onEvent(AddEditMeetingEvent.MinutesChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Gray)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "min")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.location,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "location")
                },
                onValueChange = {
                    viewModel.onEvent(AddEditMeetingEvent.LocationChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Location")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Meeting members"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Members")
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier
                        .background(Color.Blue),
                    onClick = {viewModel.onEvent(AddEditMeetingEvent.SearchUsersButtonClicked) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Search members",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                onClick = { viewModel.onEvent(AddEditMeetingEvent.SaveButtonClicked) },
            ) {
                Text(text = "Save")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
                    ){
                Button(onClick = { viewModel.onEvent(AddEditMeetingEvent.GoBackButtonClicked) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Go back"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Search",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = state.searchedUser,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                },
                onValueChange = {
                    viewModel.onEvent(AddEditMeetingEvent.SearchedUserChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Search...")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if(state.searchedUser.length > 1) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(
                        items = state.memberHashMap.entries.filter { entry ->
                            entry.key.name.lowercase().startsWith(state.searchedUser.lowercase())
                                    || entry.key.name.lowercase().endsWith(state.searchedUser.lowercase())
                        },
                        key = { it.key.userId }
                    ) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Checkbox(
                                checked = entry.value,
                                onCheckedChange = { checked ->
                                    viewModel.onEvent(AddEditMeetingEvent.UserChecked(entry.key, checked))
                                })
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = entry.key.name,
                                fontSize = 18.sp
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .align(Alignment.CenterHorizontally),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}