package com.aethyss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _response = MutableStateFlow("")
    val response: StateFlow<String> = _response

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                val reply = Network.api.chat(ChatRequest(message))
                _response.value = reply.reply
            } catch (e: Exception) {
                _response.value = "Backend offline"
            }
        }
    }
}
