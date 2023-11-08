package presentation.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

class ChatScreen(
    private val modifier: Modifier = Modifier,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ChatScreenModel>()

        Screen(modifier = modifier,)
    }

    @Composable
    private fun Screen(
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Text("Chat Screen")
        }
    }

}
