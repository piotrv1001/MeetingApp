package com.vassev.meetingapp.presentation.register.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.register.RegisterEvent
import com.vassev.meetingapp.presentation.register.RegisterViewodel
import com.vassev.meetingapp.presentation.util.Screen

@Composable
fun RegisterScreen(
    viewModel: RegisterViewodel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when(result) {
                is Resource.Success -> {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(0)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Error: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {}
            }
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Register",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = state.email,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "email")
                },
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.EmailChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Email")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.password,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Password, contentDescription = "password")
                },
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.PasswordChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Password")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.name,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "name")
                },
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.NameChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Name")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.location,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "location")
                },
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.LocationChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Location")
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                onClick = { viewModel.onEvent(RegisterEvent.RegisterButtonClicked) },
            ) {
                Text(text = "Register")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                onClick = { navController.navigate(Screen.LoginScreen.route) },
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
            ) {
                Text("Login")
            }
        }
    }
}