package presentation.screen.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import model.ModelLibrary
import presentation.designsystem.component.ThemeSwitch
import presentation.designsystem.component.MainButton
import presentation.screen.chat.ChatScreen
import presentation.screen.main.components.ModelSelect
import presentation.screen.main.components.ServerHandler

class MainScreen(
    private val modifier: Modifier = Modifier,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MainScreenModel>()
        val serverUiState by screenModel.serverUiState.collectAsState()
        val modelListUiState by screenModel.modelListUiState.collectAsState()
        val downloadModelUiState by screenModel.downloadModelUiState.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.startServer()
            screenModel.updateModelList()
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
            Row(
                modifier = modifier,
            ) {
                SideBar(
                    serverUiState = serverUiState,
                    modelListUiState = modelListUiState,
                    downloadModelUiState = downloadModelUiState,
                    onDownloadModel = onDownloadModel,
                    onRemoveModel = onRemoveModel,
                    onStartServer = onStartServer,
                    onStopServer = onStopServer,
                )

                CurrentScreen()
            }
        }
    }

    @Composable
    private fun SideBar(
        modifier: Modifier = Modifier,
        serverUiState: ServerUiState,
        modelListUiState: ModelListUiState,
        downloadModelUiState: DownloadModelUiState,
        onDownloadModel: (model: ModelLibrary) -> Unit,
        onRemoveModel: (model: ModelLibrary) -> Unit,
        onStartServer: () -> Unit,
        onStopServer: () -> Unit,
    ) {
        var expended by remember { mutableStateOf(false) }
        val width: Dp by animateDpAsState(if (expended) 160.dp else 48.dp)
        val expandIconRotation: Float by animateFloatAsState(if (expended) 0f else 180f)

        Column(
            modifier = modifier
                .fillMaxHeight()
                .width(width)
                .background(MaterialTheme.colors.surface)
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            ModelSelect(
                modifier = Modifier.height(32.dp),
                isExpanded = expended,
                modelListUiState = modelListUiState,
                onDownloadModel = onDownloadModel,
                downloadModelUiState = downloadModelUiState,
                isServerRunning = serverUiState.isRunning(),
                onRemoveModel = onRemoveModel,
            )

            ThemeSwitch(modifier = Modifier.padding(vertical = 8.dp), isExpanded = expended)

            ServerHandler(
                modifier = Modifier.padding(),
                isExpanded = expended,
                serverUiState = serverUiState,
                onStart = onStartServer,
                onStop = onStopServer,
            )

            MainButton(modifier = Modifier.size(32.dp), onClick = { expended = !expended }) {
                Icon(
                    modifier = Modifier.rotate(expandIconRotation),
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                )
            }

        }
    }
}
