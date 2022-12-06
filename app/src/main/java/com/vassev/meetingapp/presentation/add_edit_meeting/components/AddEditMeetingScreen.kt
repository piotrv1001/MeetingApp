package com.vassev.meetingapp.presentation.add_edit_meeting.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.vassev.meetingapp.data.remote.dto.UserDTO
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.add_edit_meeting.AddEditMeetingEvent
import com.vassev.meetingapp.presentation.add_edit_meeting.AddEditMeetingViewmodel

@Composable
fun AddEditMeetingScreen(
    viewModel: AddEditMeetingViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val filteredUsers by viewModel.filteredUsers.collectAsState()
    val selectedUsers by viewModel.selectedUsers.collectAsState()

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
                .fillMaxSize(),
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
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = "Meeting members"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Members (${selectedUsers.size})")
                }
                Button(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                    onClick = {viewModel.onEvent(AddEditMeetingEvent.SearchUsersButtonClicked) },
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
                        imageVector = Icons.Default.Add,
                        contentDescription = "Search members",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                    items = selectedUsers.toList()
                        .sortedWith { x: Pair<UserDTO, Boolean>, y: Pair<UserDTO, Boolean> -> if (x.first.userId == viewModel.userId) -1 else 1 },
                    key = {
                        it.first.userId
                    }
                ) { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        val username = if(entry.first.userId == viewModel.userId) {
                            "${entry.first.name} (Me)"
                        } else {
                            entry.first.name
                        }
                        Text(
                            text = username,
                            fontSize = 18.sp
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigateUp() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancel",
                    )
                }
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { viewModel.onEvent(AddEditMeetingEvent.SaveButtonClicked) },
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 18.dp
                    ),
                ) {
                    Text(text = "Save")
                }
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
                IconButton(onClick = { viewModel.onEvent(AddEditMeetingEvent.GoBackButtonClicked) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Go back",
                        tint = MaterialTheme.colors.primary
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(
                        items = filteredUsers.toList(),
                        key = {
                            it.first.userId
                        }
                    ) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Checkbox(
                                checked = entry.second,
                                onCheckedChange = { checked ->
                                    viewModel.onEvent(AddEditMeetingEvent.UserChecked(entry.first, checked))
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = Color.Gray,
                                    checkedColor = Color.Gray,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = entry.first.name,
                                fontSize = 18.sp
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(200.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Invited users (${selectedUsers.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                    items = selectedUsers.toList()
                        .sortedWith { x: Pair<UserDTO, Boolean>, y: Pair<UserDTO, Boolean> -> if (x.first.userId == viewModel.userId) -1 else 1 },
                    key = {
                        it.first.userId
                    }
                ) { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if(entry.first.userId == viewModel.userId) {
                            Text(
                                text = "${entry.first.name} (Me)",
                                fontSize = 18.sp
                            )
                        } else {
                            Checkbox(
                                checked = entry.second,
                                onCheckedChange = { checked ->
                                    viewModel.onEvent(AddEditMeetingEvent.UserChecked(entry.first, checked))
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = Color.Gray,
                                    checkedColor = Color.Gray,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = entry.first.name,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}