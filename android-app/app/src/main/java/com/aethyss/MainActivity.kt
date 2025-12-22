package com.aethyss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AethyssScreen()
        }
    }
}

@Composable
fun AethyssScreen(viewModel: MainViewModel = viewModel()) {
    var input by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("Waiting for input…") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Aethyss", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = response)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter message") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                loading = true
                viewModel.sendMessage(
                    message = input,
                    onResult = {
                        response = it
                        loading = false
                    },
                    onError = {
                        response = "Error: $it"
                        loading = false
                    }
                )
            },
            enabled = !loading
        ) {
            Text(if (loading) "Sending…" else "Send")
        }
    }
}
