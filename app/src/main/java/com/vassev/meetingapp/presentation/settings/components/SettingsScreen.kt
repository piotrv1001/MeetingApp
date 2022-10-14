package com.vassev.meetingapp.presentation.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.presentation.settings.SettingsEvent
import com.vassev.meetingapp.presentation.settings.SettingsViewmodel
import com.vassev.meetingapp.presentation.util.Screen

@Composable
fun SettingsScreen(
    viewModel: SettingsViewmodel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.settingsResults.collect { result ->
            when(result) {
                "log_out" -> {
                    navController.navigate(Screen.LoginScreen.route){
                        popUpTo(Screen.SettingsScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Settings screen"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { viewModel.onEvent(SettingsEvent.LogOutButtonClicked) },
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 12.dp,
                end = 20.dp,
                bottom = 12.dp
            )
        ) {
            Icon(
                Icons.Filled.Logout,
                contentDescription = "Log out",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Log out")
        }
    }
}