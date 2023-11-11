package presentation.screen.chat.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PromptInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onGenerateCompletion: (prompt: String) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f).heightIn(max = 160.dp),
            value = value,
            onValueChange = onValueChange,
        )

        IconButton(
            onClick = { if (value.isNotEmpty()) onGenerateCompletion(value) },
        ) {
            Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
        }
    }
}