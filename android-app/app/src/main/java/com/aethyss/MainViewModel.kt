package com.aethyss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun sendMessage(
        message: String,
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = Network.api.chat(
                    ChatRequest(message)
                )
                onResult(response)
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}
