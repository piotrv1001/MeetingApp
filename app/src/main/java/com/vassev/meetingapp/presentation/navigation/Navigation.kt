package com.vassev.meetingapp.presentation.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vassev.meetingapp.presentation.add_edit_meeting.components.AddEditMeetingScreen
import com.vassev.meetingapp.presentation.calendar.components.CalendarScreen
import com.vassev.meetingapp.presentation.chat.components.ChatScreen
import com.vassev.meetingapp.presentation.generate_time.components.GenerateTimeScreen
import com.vassev.meetingapp.presentation.home.components.HomeScreen
import com.vassev.meetingapp.presentation.login.components.LoginScreen
import com.vassev.meetingapp.presentation.meeting_info.components.MeetingInfoScreen
import com.vassev.meetingapp.presentation.plans.components.PlansScreen
import com.vassev.meetingapp.presentation.register.components.RegisterScreen
import com.vassev.meetingapp.presentation.settings.components.SettingsScreen
import com.vassev.meetingapp.presentation.shared.SharedPlanViewmodel
import com.vassev.meetingapp.presentation.util.Screen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = Screen.RegisterScreen.route, modifier = modifier, route = "Parent") {
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(
            route = Screen.AddEditMeetingScreen.route + "?meetingId={meetingId}",
            arguments = listOf(
                navArgument(
                    name = "meetingId"
                ){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            AddEditMeetingScreen(navController = navController)
        }
        composable(route = Screen.CalendarScreen.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("Parent")
            }
            val sharedPlanViewmodel = hiltViewModel<SharedPlanViewmodel>(parentEntry)
            CalendarScreen(navController = navController, viewModel = sharedPlanViewmodel)
        }
        composable(
            route = Screen.ChatScreen.route + "/{meetingId}",
            arguments = listOf(
                navArgument(
                    name = "meetingId"
                ){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            ChatScreen(navController = navController, meetingId = it.arguments?.getString("meetingId"))
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.MeetingInfoScreen.route + "/{meetingId}",
            arguments = listOf(
                navArgument(
                    name = "meetingId"
                ){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            MeetingInfoScreen(navController = navController, meetingId = it.arguments?.getString("meetingId"))
        }
        composable(
            route = Screen.PlansScreen.route
        ) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("Parent")
            }
            val sharedPlanViewmodel = hiltViewModel<SharedPlanViewmodel>(parentEntry)
            PlansScreen(
                navController = navController,
                viewModel = sharedPlanViewmodel
            )
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
        composable(
            route = Screen.GenerateTimeScreen.route + "/{meetingId}",
            arguments = listOf(
                navArgument(
                    name = "meetingId"
                ){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            GenerateTimeScreen(navController = navController, meetingId = it.arguments?.getString("meetingId"))
        }
    }
}