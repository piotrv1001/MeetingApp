package com.vassev.meetingapp.presentation.plans.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vassev.meetingapp.domain.model.PlanWithType
import com.vassev.meetingapp.presentation.shared.SharedPlanState

@Composable
fun DialogContent(
    plan: PlanWithType,
    closeDialog: () -> Unit,
    state: SharedPlanState,
    deleteOnceClick: () -> Unit,
    deleteAllClick: () -> Unit,
    deleteConfirmClick: () -> Unit,
    leaveMeetingClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if(plan.name == "") "Delete plan?" else "Leave meeting?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = closeDialog,
                    modifier = Modifier.then(Modifier.size(24.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close dialog"
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            if(plan.repeat) {
                Column {
                    Row {
                       Text(
                           text = "Delete on this day"
                       )
                       Spacer(modifier = Modifier.width(16.dp))
                       RadioButton(
                           selected = !state.removeEveryWeek,
                           onClick = deleteOnceClick
                       )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(
                            text = "Delete every week"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = state.removeEveryWeek,
                            onClick = deleteAllClick
                        )
                    }
                }
            } else {
                Text(
                    text = if(plan.name == "") "The selected plan will be permanently removed" else
                        "This will permanently remove the meeting from your meetings list"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = closeDialog,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = if(plan.name == "") deleteConfirmClick else leaveMeetingClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(
                        text = if(plan.name == "") "Delete" else "Leave",
                        color = Color.Red
                    )
                }
            }
        }
    }
}