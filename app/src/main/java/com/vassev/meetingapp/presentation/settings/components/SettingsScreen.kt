package com.vassev.meetingapp.presentation.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    viewModel: SettingsViewmodel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark theme")
            Switch(
                checked = state.isDarkThemeEnabled,
                onCheckedChange = { viewModel.onEvent(SettingsEvent.DarkThemeSwitchPressed(checked = it)) }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.onEvent(SettingsEvent.LogOutButtonClicked) },
        ) {
            Icon(
                Icons.Filled.PowerSettingsNew,
                contentDescription = "Log out",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Log out")
        }
    }
}