package com.vassev.meetingapp.presentation.register.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.login.LoginEvent
import com.vassev.meetingapp.presentation.register.RegisterEvent
import com.vassev.meetingapp.presentation.register.RegisterViewodel
import com.vassev.meetingapp.presentation.shared.components.GradientButton
import com.vassev.meetingapp.presentation.util.Screen

@Composable
fun RegisterScreen(
    viewModel: RegisterViewodel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
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
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Error: ${result.message}",
                        duration = SnackbarDuration.Short
                    )
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
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            Column(
              modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF36d1dc),
                                    Color(0xFF5b86e5)
                                )
                            )
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Register",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .drawBehind {
                            val cornerRadius = CornerRadius(50.dp.toPx(), 50.dp.toPx())
                            val path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        rect = Rect(
                                            offset = Offset(0f, -100f),
                                            size = Size(size.width, 100f),
                                        ),
                                        topLeft = cornerRadius,
                                        topRight = cornerRadius,
                                    )
                                )
                            }
                            drawPath(path, color = Color.White)
                        }
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Column {
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
                        var passwordVisible: Boolean by remember { mutableStateOf(false) }
                        TextField(
                            value = state.password,
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = "password")
                            },
                            onValueChange = {
                                viewModel.onEvent(RegisterEvent.PasswordChanged(it))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(text = "Password")
                            },
                            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = { passwordVisible = !passwordVisible }){
                                    Icon(imageVector  = image, description)
                                }
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
                    }
                    Column {
                        GradientButton(
                            text = "Register",
                            gradient = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF36d1dc),
                                    Color(0xFF5b86e5)
                                )
                            ),
                            textColor = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            onClick = { viewModel.onEvent(RegisterEvent.RegisterButtonClicked) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Divider(
                                modifier = Modifier.weight(3f),
                                thickness = 1.dp,
                                color = Color.Gray
                            )
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "or",
                                    color = Color.DarkGray,
                                )
                            }
                            Divider(
                                modifier = Modifier.weight(3f),
                                thickness = 1.dp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth(),
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
        }
    }
}