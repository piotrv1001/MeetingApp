package com.vassev.meetingapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.presentation.home.HomeViewmodel
import com.vassev.meetingapp.presentation.util.Screen

@Composable
fun HomeScreen(
    viewModel: HomeViewmodel = hiltViewModel(),
    navController: NavController
) {
    val userId = viewModel.userId
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Hello, your userId = $userId"
        )
        Button(
            modifier = Modifier
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
            onClick = { navController.navigate(Screen.AddEditMeetingScreen.route) },
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
                contentDescription = "Add meeting",
                tint = Color.White
            )
        }
    }
}