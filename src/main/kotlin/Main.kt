import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import data.di.dataModule
import di.viewModelModule
import org.koin.core.context.startKoin
import presentation.screen.ChatScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background) {
            Navigator(ChatScreen())
        }
    }
}

fun main() = application {
    startKoin {
        modules(dataModule, viewModelModule)
    }

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
