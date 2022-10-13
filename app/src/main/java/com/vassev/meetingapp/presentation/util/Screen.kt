package com.vassev.meetingapp.presentation.util

sealed class Screen(val route: String) {

    object AddEditMeetingScreen: Screen("add_edit_meeting_screen")
    object CalendarScreen: Screen("calendar_screen")
    object ChatScreen: Screen("chat_screen")
    object HomeScreen: Screen("home_screen")
    object LoginScreen: Screen("login_screen")
    object MeetingInfoScreen: Screen("meeting_info_screen")
    object PlansScreen: Screen("plans_screen")
    object RegisterScreen: Screen("register_screen")
    object SearchScreen: Screen("search_screen")
    object SettingsScreen: Screen("settings_screen")
}
