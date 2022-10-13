package com.vassev.meetingapp.presentation.register.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.presentation.register.RegisterViewmodel
import com.vassev.meetingapp.presentation.util.Screen

@Composable
fun RegisterScreen(
    viewModel: RegisterViewmodel = hiltViewModel(),
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
            Text("Register")
        }
    }

}