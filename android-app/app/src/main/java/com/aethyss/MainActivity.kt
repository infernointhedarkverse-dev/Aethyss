package com.aethyss

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aethyss.ui.theme.AethyssTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContent {
                AethyssTheme {
                    App()
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Fatal UI crash", e)
        }
    }
}
