package com.aethyss.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.navigationBarsWithImePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aethyss.data.ChatMessage
import com.aethyss.viewmodel.ChatViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    val error by viewModel.error.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(title = { Text("Aethyss") })
        },
        bottomBar = {
            ChatInputBar(
                isSending = isSending,
                onSend = viewModel::sendMessage
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (messages.isEmpty()) {
                Text(
                    text = "Start the conversation — ask anything.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    itemsIndexed(messages) { _, msg ->
                        MessageBubble(
                            message = msg,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                    item { Spacer(Modifier.height(56.dp)) }
                }
            }

            error?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ChatInputBar(
    isSending: Boolean,
    onSend: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    Surface(tonalElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsWithImePadding()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message…") },
                maxLines = 5
            )

            IconButton(
                enabled = text.isNotBlank() && !isSending,
                onClick = {
                    onSend(text.trim())
                    text = ""
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    val shape: Shape =
        if (message.isUser)
            RoundedCornerShape(12.dp, 12.dp, 4.dp, 12.dp)
        else
            RoundedCornerShape(12.dp, 12.dp, 12.dp, 4.dp)

    Surface(
        shape = shape,
        color = if (message.isUser)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.widthIn(max = 280.dp)
    ) {
        Text(
            text = message.text,
            modifier = Modifier.padding(12.dp),
            color = if (message.isUser)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
