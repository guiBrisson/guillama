package presentation.screen.chat

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.repository.OllamaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Completion
import model.ResultOf

class ChatScreenModel(
    private val ollamaRepository: OllamaRepository,
) : ScreenModel {
    private val _aiCompletionUiState = MutableStateFlow<AiCompletionUiState>(AiCompletionUiState.Idle)
    val aiCompletionUiState: StateFlow<AiCompletionUiState> = _aiCompletionUiState.asStateFlow()

    private val _chatHistoryUiState = MutableStateFlow(ChatHistoryUiState())
    val chatHistoryUiState: StateFlow<ChatHistoryUiState> = _chatHistoryUiState.asStateFlow()

    fun generateCompletion(modelName: String, prompt: String) {
        screenModelScope.launch(Dispatchers.IO) {
            ollamaRepository.generateCompletion(modelName, prompt).collect { result ->
                when (result) {
                    is ResultOf.Success -> onGenerationSuccess(result.value)
                    is ResultOf.Failure -> {
                        _aiCompletionUiState.update { AiCompletionUiState.Failure(result.exception) }
                    }
                    else -> Unit
                }

            }
        }
    }

    private fun onGenerationSuccess(completion: Completion) {
        _aiCompletionUiState.update { AiCompletionUiState.GeneratingResponse }

        // First time reaching this function
        if (_chatHistoryUiState.value.messages.isNotEmpty() && _chatHistoryUiState.value.messages.last().isFromUser){
            addNewMessageToHistory(ChatMessage(value = completion.response.trim(), isFromUser = false))
            return
        }

        // Appending last message's value
        val currentChatHistoryMessages = _chatHistoryUiState.value.messages.toMutableList()
        val lastIndex = currentChatHistoryMessages.lastIndex
        val lastMessageUpdated = currentChatHistoryMessages[lastIndex].apply {
            value += completion.response
        }

        currentChatHistoryMessages[lastIndex] = lastMessageUpdated


        _chatHistoryUiState.update { ChatHistoryUiState(currentChatHistoryMessages) }


        // Finished generating response
        if (completion.done) {
            _aiCompletionUiState.update { AiCompletionUiState.Idle }
        }
    }

    fun addNewMessageToHistory(chatMessage: ChatMessage) {
        val currentChatHistory = _chatHistoryUiState.value.messages
        _chatHistoryUiState.update { ChatHistoryUiState(currentChatHistory + chatMessage) }
    }
}
