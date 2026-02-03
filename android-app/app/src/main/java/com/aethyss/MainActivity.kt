package com.aethyss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aethyss.navigation.NavGraph
import com.aethyss.ui.theme.AethyssTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AethyssTheme {
                NavGraph()
            }
        }
    }
}
