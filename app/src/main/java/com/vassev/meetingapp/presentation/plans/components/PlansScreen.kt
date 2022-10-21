package com.vassev.meetingapp.presentation.plans.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.vassev.meetingapp.R
import com.vassev.meetingapp.domain.util.DateUtil
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.add_edit_meeting.AddEditMeetingEvent
import com.vassev.meetingapp.presentation.shared.SharedPlanEvent
import com.vassev.meetingapp.presentation.shared.SharedPlanViewmodel
import com.vassev.meetingapp.presentation.util.Screen
import java.time.LocalDate

@Composable
fun PlansScreen(
    viewModel: SharedPlanViewmodel,
    navController: NavController,
) {
    val state by viewModel.state.collectAsState()
    val dayOfWeek = viewModel.dayOfWeek
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.sharedPlanResults.collect { result ->
            when (result) {
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Error: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Success -> {
                    Toast.makeText(
                        context,
                        "Successfully added plan!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                }
            }
        }
    }
    val dayOfWeekName = DateUtil.getDayOfWeekName(dayOfWeek)
    if(state.showDialog) {
        Dialog(
            onDismissRequest = { viewModel.onEvent(SharedPlanEvent.CloseDialogClicked) }
        ) {
            DialogContent(
                removingRepeatedPlan = state.removingRepeatedPlan,
                closeDialog = { viewModel.onEvent(SharedPlanEvent.CloseDialogClicked) },
                state = state,
                deleteOnceClick = { viewModel.onEvent(SharedPlanEvent.RemoveOnceRadioButtonClicked) },
                deleteAllClick = { viewModel.onEvent(SharedPlanEvent.RemoveALlRadioButtonClicked) }
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
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
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = buildAnnotatedString {
                append("When are you available\non ")
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append(dayOfWeekName)
                }
                append(" ?")
            },
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "From",
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                value = state.fromHour,
                onValueChange = {
                    viewModel.onEvent(SharedPlanEvent.FromHourChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ":"
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                value = state.fromMinute,
                onValueChange = {
                    viewModel.onEvent(SharedPlanEvent.FromMinuteChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "To",
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                value = state.toHour,
                onValueChange = {
                    viewModel.onEvent(SharedPlanEvent.ToHourChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ":"
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                value = state.toMinute,
                onValueChange = {
                    viewModel.onEvent(SharedPlanEvent.ToMinuteChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Every $dayOfWeekName"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Checkbox(
                    checked = state.isRepeatChecked,
                    onCheckedChange = {
                        viewModel.onEvent(SharedPlanEvent.RepeatCheckChanged(it))
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = Color.Gray,
                        checkedColor = Color.Gray,
                        checkmarkColor = Color.White
                    )
                )
            }
            Button(
                modifier = Modifier
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                onClick = { viewModel.onEvent(SharedPlanEvent.AddPlanButtonClicked) },
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
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add plan",
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = state.plans
                ) { plan ->
                    Row {
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
                            Text(
                                text = DateUtil.getFormattedPlan(
                                    plan.fromHour,
                                    plan.fromMinute,
                                    plan.toHour,
                                    plan.toMinute
                                ),
                                color = Color(0xFF06A94D),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { viewModel.onEvent(SharedPlanEvent.RemovePlanButtonClicked(plan.repeat)) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.LightGray,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                tint = Color.Black,
                                contentDescription = "Remove plan",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}