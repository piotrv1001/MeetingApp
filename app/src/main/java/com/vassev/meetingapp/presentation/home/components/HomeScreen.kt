package com.vassev.meetingapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                .background(Color.Blue),
            onClick = { navController.navigate(Screen.AddEditMeetingScreen.route) },
            shape = RoundedCornerShape(10)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add meeting",
                tint = Color.White
            )
        }
    }
}