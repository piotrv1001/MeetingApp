package com.vassev.meetingapp.presentation.chat.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.vassev.meetingapp.domain.util.Resource
import com.vassev.meetingapp.presentation.chat.ChatEvent
import com.vassev.meetingapp.presentation.chat.ChatViewmodel
import kotlinx.coroutines.flow.collect

@Composable
fun ChatScreen(
    viewModel: ChatViewmodel = hiltViewModel(),
    navController: NavController,
    meetingId: String?
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.chatResults.collect { result ->
            when(result) {
                is Resource.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START) {
                viewModel.onEvent(ChatEvent.JoinChatRoom)
            } else if(event == Lifecycle.Event.ON_STOP) {
                viewModel.onEvent(ChatEvent.LeaveChatRoom)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val state by viewModel.state.collectAsState()
    if(state.isLoading) {
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
                .padding(16.dp)

        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ){
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                items(state.messages) { message ->
                    val isOwnMessage = message.userId == viewModel.userId
                    Box(
                        contentAlignment = if(isOwnMessage) {
                            Alignment.CenterEnd
                        } else Alignment.CenterStart,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .drawBehind {
                                    val cornerRadius = 10.dp.toPx()
                                    val triangleHeight = 20.dp.toPx()
                                    val triangleWidth = 25.dp.toPx()
                                    val trianglePath = Path().apply {
                                        if (isOwnMessage) {
                                            moveTo(size.width, size.height - cornerRadius)
                                            lineTo(size.width, size.height + triangleHeight)
                                            lineTo(
                                                size.width - triangleWidth,
                                                size.height - cornerRadius
                                            )
                                            close()
                                        } else {
                                            moveTo(0f, size.height - cornerRadius)
                                            lineTo(0f, size.height + triangleHeight)
                                            lineTo(triangleWidth, size.height - cornerRadius)
                                            close()
                                        }
                                    }
                                    drawPath(
                                        path = trianglePath,
                                        color = if (isOwnMessage) Color.Green else Color.DarkGray
                                    )
                                }
                                .background(
                                    color = if (isOwnMessage) Color.Green else Color.DarkGray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = message.userId,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = message.text,
                                color = Color.White
                            )
                            Text(
                                text = message.formattedTime,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = state.messageText,
                    onValueChange = { viewModel.onEvent(ChatEvent.MessageChanged(it)) },
                    placeholder = {
                        Text(text = "Enter a message")
                    },
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { viewModel.onEvent(ChatEvent.SendMessage) }
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                }
            }
        }

    }
}