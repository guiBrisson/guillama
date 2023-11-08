package presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Density

@Composable
fun loadSvgPainter(resourcePath: String, density: Density = LocalDensity.current): Painter {
    return useResource(resourcePath) { loadSvgPainter(it, density) }
}