package presentation.screen.main.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import presentation.designsystem.component.TopBarButton
import presentation.screen.main.ServerUiState

@Composable
fun ServerHandle(
    modifier: Modifier = Modifier,
    serverUiState: ServerUiState,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    val icon = if (serverUiState.isRunning()) Icons.Outlined.Stop else Icons.Outlined.PlayArrow
    val backgroundColor = if (serverUiState.isRunning()) MaterialTheme.colors.error else MaterialTheme.colors.primary

    TopBarButton(
        modifier = modifier,
        onClick = { if (serverUiState.isRunning()) onStop() else onStart() },
        backgroundColor = backgroundColor,
    ) {
        Icon(modifier = Modifier.size(24.dp), imageVector = icon, contentDescription = null, tint = Color.White)
    }
}
