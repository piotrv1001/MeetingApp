package com.vassev.meetingapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vassev.meetingapp.presentation.add_edit_meeting.components.AddEditMeetingScreen
import com.vassev.meetingapp.presentation.calendar.components.CalendarScreen
import com.vassev.meetingapp.presentation.chat.components.ChatScreen
import com.vassev.meetingapp.presentation.home.components.HomeScreen
import com.vassev.meetingapp.presentation.login.components.LoginScreen
import com.vassev.meetingapp.presentation.meeting_info.components.MeetingInfoScreen
import com.vassev.meetingapp.presentation.plans.components.PlansScreen
import com.vassev.meetingapp.presentation.register.components.RegisterScreen
import com.vassev.meetingapp.presentation.search.components.SearchScreen
import com.vassev.meetingapp.presentation.settings.components.SettingsScreen
import com.vassev.meetingapp.presentation.util.Screen

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.RegisterScreen.route) {
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.AddEditMeetingScreen.route) {
            AddEditMeetingScreen(navController = navController)
        }
        composable(route = Screen.CalendarScreen.route) {
            CalendarScreen(navController = navController)
        }
        composable(route = Screen.ChatScreen.route) {
            ChatScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.MeetingInfoScreen.route) {
            MeetingInfoScreen(navController = navController)
        }
        composable(route = Screen.PlansScreen.route) {
            PlansScreen(navController = navController)
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
    }
}