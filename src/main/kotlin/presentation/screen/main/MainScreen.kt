package presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import model.ModelLibrary
import presentation.designsystem.component.ThemeSwitch
import presentation.screen.chat.ChatScreen
import presentation.screen.main.components.ModelSelect
import presentation.screen.main.components.ServerHandle

class MainScreen(
    private val modifier: Modifier = Modifier,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MainScreenModel>()
        val serverUiState by screenModel.serverUiState.collectAsState()
        val modelListUiState by screenModel.modelListUiState.collectAsState()
        val downloadModelUiState by screenModel.downloadModelUiState.collectAsState()

        // Todo: find a better way to do the first model list update
        LaunchedEffect(serverUiState) {
            if (serverUiState.isRunning()) {
                screenModel.updateModelList()
            }
        }

        Screen(
            modifier = modifier,
            serverUiState = serverUiState,
            modelListUiState = modelListUiState,
            downloadModelUiState = downloadModelUiState,
            onDownloadModel = screenModel::downloadModel,
            onRemoveModel = screenModel::removeModel,
            onStartServer = screenModel::startServer,
            onStopServer = screenModel::stopServer,
        )
    }

    @Composable
    private fun Screen(
        modifier: Modifier = Modifier,
        serverUiState: ServerUiState,
        modelListUiState: ModelListUiState,
        downloadModelUiState: DownloadModelUiState,
        onDownloadModel: (model: ModelLibrary) -> Unit,
        onRemoveModel: (model: ModelLibrary) -> Unit,
        onStartServer: () -> Unit,
        onStopServer: () -> Unit,
    ) {
        Navigator(ChatScreen()) {
            Scaffold(
                modifier = modifier,
                topBar = {
                    AppTopBar(
                        serverUiState = serverUiState,
                        modelListUiState = modelListUiState,
                        downloadModelUiState = downloadModelUiState,
                        onDownloadModel = onDownloadModel,
                        onRemoveModel = onRemoveModel,
                        onStartServer = onStartServer,
                        onStopServer = onStopServer,
                    )
                },
                content = { CurrentScreen() },
            )
        }
    }

    @Composable
    private fun AppTopBar(
        modifier: Modifier = Modifier,
        serverUiState: ServerUiState,
        modelListUiState: ModelListUiState,
        downloadModelUiState: DownloadModelUiState,
        onDownloadModel: (model: ModelLibrary) -> Unit,
        onRemoveModel: (model: ModelLibrary) -> Unit,
        onStartServer: () -> Unit,
        onStopServer: () -> Unit,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(MaterialTheme.colors.surface)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            ServerHandle(
                modifier = Modifier.size(32.dp),
                serverUiState = serverUiState,
                onStart = onStartServer,
                onStop = onStopServer,
            )

            Spacer(modifier = Modifier.weight(1f))

            ThemeSwitch(modifier = Modifier.padding(horizontal = 8.dp).size(32.dp))

            ModelSelect(
                modifier = Modifier.height(32.dp),
                modelListUiState = modelListUiState,
                onDownloadModel = onDownloadModel,
                downloadModelUiState = downloadModelUiState,
                isServerRunning = serverUiState.isRunning(),
                onRemoveModel = onRemoveModel,
            )

        }
    }
}
