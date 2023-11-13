package presentation.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.designsystem.theme.ThemeState
import presentation.utils.loadSvgPainter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ThemeSwitch(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
) {
    var isDarkMode by remember { mutableStateOf(ThemeState.isDark) }
    val setDarkMode: (Boolean) -> Unit = { isDark ->
        isDarkMode = isDark
        ThemeState.isDark = isDark
    }
    val innerModifier =
        modifier.clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colors.background).padding(4.dp)

    AnimatedContent(isExpanded) { expanded ->
        if (expanded) {
            ExtendedThemeSwitch(innerModifier, isDarkMode, setDarkMode)
        } else {
            CompactThemeSwitch(innerModifier, isDarkMode, setDarkMode)
        }
    }
}

@Composable
private fun ExtendedThemeSwitch(
    modifier: Modifier,
    isDarkMode: Boolean,
    setDarkMode: (Boolean) -> Unit
) {
    Row(modifier = modifier) {
        // Light button
        ThemeSwitchButton(
            active = !isDarkMode,
            onClick = { setDarkMode(false) }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = loadSvgPainter("icons/ic_sun.svg"),
                contentDescription = null,
                tint = if (!isDarkMode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
            )
            Text(text = "Light", fontSize = 14.sp)
        }

        // Dark button
        ThemeSwitchButton(
            active = isDarkMode,
            onClick = { setDarkMode(true) }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = loadSvgPainter("icons/ic_moon.svg"),
                contentDescription = null,
                tint = if (isDarkMode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
            )
            Text(text = "Dark", fontSize = 14.sp)
        }
    }
}

@Composable
private fun CompactThemeSwitch(
    modifier: Modifier,
    isDarkMode: Boolean,
    setDarkMode: (Boolean) -> Unit
) {
    Column(modifier = modifier) {
        // Light button
        ThemeSwitchButton(
            active = !isDarkMode,
            onClick = { setDarkMode(false) }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = loadSvgPainter("icons/ic_sun.svg"),
                contentDescription = null,
                tint = if (!isDarkMode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
            )
        }

        // Dark button
        ThemeSwitchButton(
            active = isDarkMode,
            onClick = { setDarkMode(true) }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = loadSvgPainter("icons/ic_moon.svg"),
                contentDescription = null,
                tint = if (isDarkMode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
            )
        }
    }
}

@Composable
fun ThemeSwitchButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val backgroundColor = if (active) MaterialTheme.colors.surface else Color.Unspecified

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = content,
    )
}
