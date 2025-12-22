package com.aethyss

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

data class ChatRequest(val message: String)
data class ChatResponse(val reply: String)

interface ApiService {
    @POST("/chat")
    fun chat(@Body request: ChatRequest): Call<ChatResponse>
}
