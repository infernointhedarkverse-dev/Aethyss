package com.aethyss

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.await

class MainViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<String>>(emptyList())
    val messages: LiveData<List<String>> = _messages

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                val response = Network.api.chat(ChatRequest(message)).await()
                _messages.value = _messages.value.orEmpty() + response.reply
            } catch (e: Exception) {
                _messages.value = _messages.value.orEmpty() + "Error: ${e.message}"
            }
        }
    }
}	
