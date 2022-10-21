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
import com.vassev.meetingapp.presentation.shared.SharedPlanState

@Composable
fun DialogContent(
    removingRepeatedPlan: Boolean,
    closeDialog: () -> Unit,
    state: SharedPlanState,
    deleteOnceClick: () -> Unit,
    deleteAllClick: () -> Unit
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
                    text = "Are you sure?",
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
            if(removingRepeatedPlan) {
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
                    text = "The selected plan will be permanently removed"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {  },
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
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Delete",
                        color = Color.Red
                    )
                }
            }
        }
    }
}