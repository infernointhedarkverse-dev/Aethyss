package com.aethyss

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val backendUrl = "http://10.0.2.2:8000/health" // emulator / CI-safe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.text = "Aethyss starting..."
        setContentView(textView)

        checkBackend(textView)
    }

    private fun checkBackend(textView: TextView) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(backendUrl)
            .build()

        thread {
            try {
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: "Empty response"

                runOnUiThread {
                    textView.text = "Backend OK:\n$body"
                }

            } catch (e: IOException) {
                Log.e("Aethyss", "Backend unreachable", e)

                runOnUiThread {
                    textView.text = "Backend offline\nApp still stable"
                }
            }
        }
    }
}
