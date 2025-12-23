package com.aethyss

import retrofit2.http.Body
import retrofit2.http.POST

data class ChatRequest(
    val message: String
)

interface ApiService {

    @POST("/chat")
    suspend fun chat(
        @Body request: ChatRequest
    ): String  // Changed from ChatResponse to String
}
