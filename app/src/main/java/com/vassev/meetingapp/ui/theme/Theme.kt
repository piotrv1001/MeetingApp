package com.vassev.meetingapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.vassev.meetingapp.presentation.settings.SettingsViewmodel

private val DarkColorPalette = darkColors(
    primary = LimeGreen200,
    primaryVariant = LimeGreen100,
    secondary = LimeGreen100,
    secondaryVariant = LimeGreen200,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = ErrorDark
)

private val LightColorPalette = lightColors(
    primary = LimeGreen400,
    primaryVariant = LimeGreen600,
    secondary = LimeGreen600,
    secondaryVariant = LimeGreen800,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = ErrorLight

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MeetingAppTheme(
    viewModel: SettingsViewmodel,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {

    val state by viewModel.state.collectAsState()
    val colors = if (state.isDarkThemeEnabled) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}