package presentation.designsystem.theme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

private val LightColorScheme = lightColors(
    primary = light_primary,
    onPrimary = light_onPrimary,
    secondary = light_secondary,
    onSecondary = light_onSecondary,
    error = light_error,
    onError = light_onError,
    background = light_background,
    onBackground = light_onBackground,
    surface = light_surface,
    onSurface = light_onSurface,
)

private val DarkColorScheme = darkColors(
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    secondary = dark_secondary,
    onSecondary = dark_onSecondary,
    error = dark_error,
    onError = dark_onError,
    background = dark_background,
    onBackground = dark_onBackground,
    surface = dark_surface,
    onSurface = dark_onSurface,
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val colors : Colors = if (ThemeState.isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content,
    )
}

object ThemeState {
    var isDark by mutableStateOf(false)
}
