package presentation.designsystem.component

import androidx.compose.material.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import presentation.designsystem.theme.ThemeState

@Composable
fun ThemeSwitch(
    modifier: Modifier = Modifier,
) {
    var checked by remember { mutableStateOf(ThemeState.isDark) }

    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            checked = it
            ThemeState.isDark = it
        }
    )
}
