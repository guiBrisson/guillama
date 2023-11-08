import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import data.di.dataModule
import di.viewModelModule
import org.koin.core.context.startKoin
import presentation.designsystem.theme.AppTheme
import presentation.screen.main.MainScreen

@Composable
fun App() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Navigator(MainScreen(modifier = Modifier.fillMaxSize()))
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
