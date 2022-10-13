package com.vassev.meetingapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.navigation.compose.rememberNavController
import com.vassev.meetingapp.presentation.navigation.BottomNavigationBar
import com.vassev.meetingapp.presentation.navigation.Navigation
import com.vassev.meetingapp.presentation.navigation.NavigationItem
import com.vassev.meetingapp.presentation.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        items = listOf(
                            NavigationItem(
                                name = "Home",
                                route = Screen.HomeScreen.route,
                                icon = Icons.Outlined.Home,
                                selectedIcon = Icons.Filled.Home,
                            ),
                            NavigationItem(
                                name = "Calendar",
                                route = Screen.CalendarScreen.route,
                                icon = Icons.Outlined.CalendarToday,
                                selectedIcon = Icons.Filled.CalendarToday,
                            ),
                            NavigationItem(
                                name = "Settings",
                                route = Screen.SettingsScreen.route,
                                icon = Icons.Outlined.Settings,
                                selectedIcon = Icons.Filled.Settings,
                            ),
                        ),
                        navController = navController,
                        onItemClick = {
                            navController.navigate(it.route)
                        }
                    )
                }
            ) {
                Navigation(navController = navController)
            }
        }
    }
}