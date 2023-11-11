package presentation.screen.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import presentation.screen.chat.ChatMessage

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    message: ChatMessage,
) {
    Row(modifier = modifier) {
        val backgroundColor = if (message.isFromUser) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.surface
        }

        val textColor = if (message.isFromUser) {
            MaterialTheme.colors.onPrimary
        } else {
            MaterialTheme.colors.onBackground
        }

        val paddingValues = if(message.isFromUser) {
            PaddingValues(start = 40.dp)
        } else {
            PaddingValues(end = 40.dp)
        }

        if (message.isFromUser) Spacer(Modifier.weight(1f))

        Text(
            modifier = Modifier
                .padding(paddingValues)
                .clip(RoundedCornerShape(4.dp))
                .background(backgroundColor)
                .padding(8.dp),
            text = message.value,
            color = textColor,
        )
    }
}
