package presentation.screen.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.designsystem.component.MainButton
import presentation.screen.main.ServerUiState

@Composable
fun ServerHandler(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    serverUiState: ServerUiState,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    val icon = if (serverUiState.isRunning()) Icons.Outlined.Pause else Icons.Outlined.PlayArrow
    val iconColor = if (serverUiState.isRunning()) MaterialTheme.colors.error else MaterialTheme.colors.primary

    val currentStatus: String = when (serverUiState) {
        is ServerUiState.Error -> "There was a problem"
        is ServerUiState.Idle -> "Start the server"
        is ServerUiState.Running -> "Server is running"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AnimatedVisibility(isExpanded) {
            Text(text = currentStatus, fontSize = 12.sp)
        }

        MainButton(
            modifier = modifier.size(32.dp),
            onClick = { if (serverUiState.isRunning()) onStop() else onStart() },
        ) {
            Icon(modifier = Modifier.size(20.dp), imageVector = icon, contentDescription = null, tint = iconColor)
        }
    }
}
