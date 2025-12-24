package com.aethyss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AethyssAppScreen()
        }
    }
}

@Composable
fun AethyssAppScreen(mainViewModel: MainViewModel = viewModel()) {
    var inputText by remember { mutableStateOf("") }
    var chatOutput by remember { mutableStateOf("Aethyss is running") }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(text = chatOutput)

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .height(56.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                    )

                    Button(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                mainViewModel.sendMessage(
                                    message = inputText,
                                    onResult = { response ->
                                        chatOutput = response
                                        inputText = ""
                                    },
                                    onError = { error ->
                                        chatOutput = error
                                    }
                                )
                            }
                        }
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    }
}
