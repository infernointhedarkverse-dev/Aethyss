package com.aethyss

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var statusView: TextView

    private val backendUrl = "http://10.0.2.2:8000/health"

    private val executor = Executors.newSingleThreadExecutor()
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statusView = TextView(this)
        statusView.textSize = 18f
        statusView.text = savedInstanceState?.getString("status")
            ?: "Aethyss initializing…"

        setContentView(statusView)

        if (savedInstanceState == null) {
            checkBackend()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("status", statusView.text.toString())
    }

    override fun onStart() {
        super.onStart()
        Log.d("Aethyss", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Aethyss", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Aethyss", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Aethyss", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
        Log.d("Aethyss", "onDestroy")
    }

    private fun checkBackend() {
        statusView.text = "Checking backend…"

        val request = Request.Builder()
            .url(backendUrl)
            .build()

        executor.execute {
            try {
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: "No response"

                runOnUiThread {
                    statusView.text = "Backend reachable\n\n$body"
                }

            } catch (e: IOException) {
                Log.e("Aethyss", "Backend check failed", e)

                runOnUiThread {
                    statusView.text =
                        "Backend offline\n\nApp is stable and running"
                }
            }
        }
    }
}

