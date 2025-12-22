package com.aethyss

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messagesView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)
        messagesView = findViewById(R.id.messages_view)

        sendButton.setOnClickListener {
            val msg = messageInput.text.toString()
            if (msg.isNotEmpty()) {
                viewModel.sendMessage(msg)
                messageInput.text.clear()
            }
        }

        viewModel.messages.observe(this) { msgs ->
            messagesView.text = msgs.joinToString("\n")
        }
    }
}

