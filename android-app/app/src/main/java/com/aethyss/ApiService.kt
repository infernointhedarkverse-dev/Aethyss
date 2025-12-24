package com.aethyss

import retrofit2.http.Body
import retrofit2.http.POST

data class ChatRequest(
    val message: String
)

data class ChatResponse(
    val reply: String
)

interface ApiService {

    @POST("/chat")
    suspend fun chat(
        @Body request: ChatRequest
    ): ChatResponse
}
