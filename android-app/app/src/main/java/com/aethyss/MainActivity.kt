package com.aethyss

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    // CHANGE THIS to your backend later if needed
    private val backendUrl = "http://10.0.2.2:8000/chat"

    private val executor = Executors.newSingleThreadExecutor()
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val scroll = ScrollView(this)
        val chatView = TextView(this).apply {
            textSize = 16f
            text = "Aethyss Chat Ready\n\n"
        }
        scroll.addView(chatView)

        val input = EditText(this).apply {
            hint = "Type your message"
        }

        val send = Button(this).apply {
            text = "Send"
        }

        root.addView(scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
            )
        )
        root.addView(input)
        root.addView(send)

        setContentView(root)

        send.setOnClickListener {
            val message = input.text.toString().trim()
            if (message.isNotEmpty()) {
                input.setText("")
                chatView.append("You: $message\n")
                sendMessage(message, chatView)
            }
        }
    }

    private fun sendMessage(message: String, chatView: TextView) {
        val json = JSONObject()
        json.put("message", message)

        val body = json.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(backendUrl)
            .post(body)
            .build()

        executor.execute {
            try {
                val response = client.newCall(request).execute()
                val reply = response.body?.string() ?: "No response"

                runOnUiThread {
                    chatView.append("Bot: $reply\n\n")
                }

            } catch (e: IOException) {
                runOnUiThread {
                    chatView.append(
                        "Bot: (backend unreachable)\n\n"
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}


