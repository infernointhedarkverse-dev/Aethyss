package com.aethyss.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aethyss.data.ChatMessage
import com.aethyss.viewmodel.ChatViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.navigationBarsWithImePadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator

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

    // Auto-scroll to bottom when messages change
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            // scroll to the last message (index = size - 1)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("Aethyss") }
            )
        },
        // Bottom bar pinned — IME-safe due to navigationBarsWithImePadding used inside ChatInputBar
        bottomBar = {
            ChatInputBar(
                isSending = isSending,
                onSend = { text ->
                    viewModel.sendMessage(text)
                }
            )
        }
    ) { paddingValues ->
        // Content area: messages in LazyColumn; bottom padding accounts for the bottomBar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (messages.isEmpty()) {
                // Placeholder when no messages
                Text(
                    text = "Start the conversation — ask anything.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(20.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        // provide some extra bottom padding so last message is not hidden behind input
                        .padding(bottom = 8.dp),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 12.dp),
                ) {
                    itemsIndexed(messages) { index, msg ->
                        MessageBubble(
                            message = msg,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                    // Spacer at the end so content doesn't stick to bottom bar
                    item {
                        Spacer(modifier = Modifier.height(56.dp))
                    }
                }
            }

            // Show a small inline error if present (above input)
            error?.let { err ->
                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = with(LocalDensity.current) { 80.dp } , start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp
                    )
                }
            }

            // Optional sending indicator (floating) when sending long replies
            if (isSending) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 96.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 6.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Thinking...",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Chat input bar - always visible and IME-safe.
 * This uses navigationBarsWithImePadding to avoid being hidden by keyboard or system bars.
 */
@Composable
fun ChatInputBar(
    isSending: Boolean,
    onSend: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }

    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsWithImePadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Type a message...") },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                maxLines = 6,
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Send button
            val enabled = text.isNotBlank() && !isSending
            IconButton(
                onClick = {
                    val trimmed = text.trim()
                    if (trimmed.isNotEmpty() && !isSending) {
                        onSend(trimmed)
                        text = ""
                    }
                },
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}

/**
 * Message bubble that adapts to light/dark themes and aligns left/right depending on sender.
 */
@Composable
fun MessageBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    val bubbleShapeUser: Shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 4.dp)
    val bubbleShapeBot: Shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 4.dp, bottomEnd = 12.dp)

    val userBg = MaterialTheme.colorScheme.primary
    val userTextColor = MaterialTheme.colorScheme.onPrimary

    val botBg = MaterialTheme.colorScheme.surfaceVariant
    val botTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier,
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (message.isUser) {
            // User bubble (right)
            Surface(
                color = userBg,
                shape = bubbleShapeUser,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.text,
                    color = userTextColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        } else {
            // Bot bubble (left)
            Surface(
                color = botBg,
                shape = bubbleShapeBot,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.text,
                    color = botTextColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = 15.sp
                )
            }
        }
    }
}
