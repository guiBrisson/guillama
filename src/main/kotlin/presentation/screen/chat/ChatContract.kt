package presentation.screen.chat

import java.lang.Exception

sealed interface AiCompletionUiState {
    object Idle : AiCompletionUiState
    object GeneratingResponse : AiCompletionUiState
    data class Failure(val exception: Exception?) : AiCompletionUiState
}

data class ChatHistoryUiState(
    val messages: List<ChatMessage> = emptyList(),
)

data class ChatMessage(
    var value: String,
    val isFromUser: Boolean,
)
