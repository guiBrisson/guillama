package presentation.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import presentation.designsystem.theme.ThemeState
import presentation.utils.loadSvgPainter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ThemeSwitch(
    modifier: Modifier = Modifier,
) {
    var checked by remember { mutableStateOf(ThemeState.isDark) }

    AnimatedContent(checked) { darkMode ->
        if (darkMode) {
            IconButton(modifier = modifier, onClick = { checked = !checked; ThemeState.isDark = checked }) {
                Icon(painter = loadSvgPainter("icons/ic_sun.svg"), contentDescription = null)
            }
        } else {
            IconButton(modifier = modifier, onClick = { checked = !checked; ThemeState.isDark = checked }) {
                Icon(painter = loadSvgPainter("icons/ic_moon.svg"), contentDescription = null)
            }
        }
    }
}
