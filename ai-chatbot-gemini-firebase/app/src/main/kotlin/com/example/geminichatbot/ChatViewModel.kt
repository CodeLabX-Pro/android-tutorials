package com.example.geminichatbot

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
)

class ChatViewModel : ViewModel() {

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-3.5-flash-lite",
            systemInstruction = content {
                text("You are CodeLabX Bot, a friendly assistant. Keep answers short and simple. Use plain text with no markdown.")
            },
        )

    private var chat = model.startChat()

    val messages = mutableStateListOf<ChatMessage>()

    var isLoading by mutableStateOf(false)
        private set

    fun newChat() {
        if (isLoading) return
        chat = model.startChat()
        messages.clear()
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank() || isLoading) return

        messages.add(ChatMessage(text = userText, isUser = true))
        messages.add(ChatMessage(text = "", isUser = false))
        val botIndex = messages.lastIndex
        isLoading = true

        viewModelScope.launch {
            try {
                chat.sendMessageStream(userText).collect { chunk ->
                    val current = messages[botIndex]
                    messages[botIndex] = current.copy(text = current.text + (chunk.text ?: ""))
                }
            } catch (e: Exception) {
                Log.e("ChatBot", "Gemini request failed", e)
                messages[botIndex] = ChatMessage(
                    text = "Oops: ${e.message ?: "something went wrong"}",
                    isUser = false,
                )
            } finally {
                isLoading = false
            }
        }
    }
}

