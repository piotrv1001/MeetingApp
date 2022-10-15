package com.vassev.meetingapp.presentation.add_edit_meeting.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    if (state.isLoading) {
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
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                onClick = { viewModel.onEvent(AddEditMeetingEvent.SaveButtonClicked) },
            ) {
                Text(text = "Save")
            }
        }
    }
}