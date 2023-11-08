package presentation.designsystem.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val interFontFamily = FontFamily(
    Font(
        resource = "fonts/inter/inter_light.ttf",
        weight = FontWeight.Light,
    ),
    Font(
        resource = "fonts/inter/inter_regular.ttf",
        weight = FontWeight.Normal,
    ),
    Font(
        resource = "fonts/inter/inter_medium.ttf",
        weight = FontWeight.Medium,
    ),
    Font(
        resource = "fonts/inter/inter_semi_bold.ttf",
        weight = FontWeight.SemiBold,
    ),
    Font(
        resource = "fonts/inter/inter_bold.ttf",
        weight = FontWeight.Bold,
    ),
)

val Typography = Typography(
    defaultFontFamily = interFontFamily
)