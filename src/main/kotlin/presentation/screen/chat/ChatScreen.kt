package presentation.screen.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import presentation.screen.chat.components.MessageItem
import presentation.screen.chat.components.PromptInput
import presentation.screen.main.components.SelectedModel

class ChatScreen(
    private val modifier: Modifier = Modifier,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ChatScreenModel>()
        val chatCompletionUiState by screenModel.aiCompletionUiState.collectAsState()
        val chatHistoryUiState by screenModel.chatHistoryUiState.collectAsState()

        Screen(
            modifier = modifier,
            aiCompletionUiState = chatCompletionUiState,
            chatHistoryUiState = chatHistoryUiState,
            onGenerateCompletion = { prompt ->
                SelectedModel.value?.modelName?.let { modelName ->
                    screenModel.generateCompletion(modelName, prompt)
                }

                screenModel.addNewMessageToHistory(ChatMessage(value = prompt, isFromUser = true))
            }
        )
    }

    @Composable
    private fun Screen(
        modifier: Modifier = Modifier,
        aiCompletionUiState: AiCompletionUiState,
        chatHistoryUiState: ChatHistoryUiState,
        onGenerateCompletion: (prompt: String) -> Unit,
    ) {
        var prompt by remember { mutableStateOf("") }
        val clearPrompt = { prompt = "" }

        val chatMaxWidth = 800.dp

        Column(
            modifier = modifier.padding(horizontal = 20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(chatHistoryUiState.messages) { message ->
                    MessageItem(
                        modifier = Modifier.widthIn(min = 400.dp, max = 800.dp).padding(vertical = 4.dp),
                        message = message,
                    )
                }
            }

            PromptInput(
                modifier = Modifier.widthIn(max = chatMaxWidth).padding(vertical = 20.dp),
                value = prompt,
                onValueChange = { prompt = it },
                onGenerateCompletion = { onGenerateCompletion(it); clearPrompt() }
            )

        }
    }

}
