package presentation.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            TopBarButton(modifier = modifier, onClick = { checked = !checked; ThemeState.isDark = checked }) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = loadSvgPainter("icons/ic_sun.svg"),
                    contentDescription = null,
                )
            }
        } else {
            TopBarButton(modifier = modifier, onClick = { checked = !checked; ThemeState.isDark = checked }) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = loadSvgPainter("icons/ic_moon.svg"),
                    contentDescription = null,
                )
            }
        }
    }
}
