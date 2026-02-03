package com.aethyss.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aethyss.ChatRequest
import com.aethyss.Network
import com.aethyss.data.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    // Exposed message list (Compose will collect this)
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Sending/loading state
    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    // Simple error state (null = no error)
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /** Public API: send a message */
    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return

        // Append user message immediately (optimistic UI)
        appendMessage(ChatMessage(text = trimmed, isUser = true))

        _isSending.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    // Reuses your existing Retrofit API
                    Network.api.chat(ChatRequest(trimmed))
                }

                // Append AI reply
                appendMessage(ChatMessage(text = response.reply, isUser = false))
            } catch (e: Exception) {
                // On error, expose message and also append a visible error bubble
                val msg = e.localizedMessage ?: "Network error"
                _error.value = msg
                appendMessage(ChatMessage(text = "Error: $msg", isUser = false))
            } finally {
                _isSending.value = false
            }
        }
    }

    /** Append message and emit new list */
    private fun appendMessage(message: ChatMessage) {
        _messages.value = _messages.value + message
    }

    /** Replace entire conversation (useful when loading from storage) */
    fun setMessages(list: List<ChatMessage>) {
        _messages.value = list
    }

    /** Clear conversation in memory (we'll add persistence later) */
    fun clearMessages() {
        _messages.value = emptyList()
    }
}
