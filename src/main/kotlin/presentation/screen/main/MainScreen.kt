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

class MainScreen(
    private val modifier: Modifier = Modifier,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MainScreenModel>()
        val modelListUiState by screenModel.modelListUiState.collectAsState()
        val downloadModelUiState by screenModel.downloadModelUiState.collectAsState()

        LaunchedEffect(modelListUiState) {
            if (modelListUiState == ModelListUiState.Init) {
                screenModel.updateModelList()
            }
        }

        Screen(
            modifier = modifier,
            modelListUiState = modelListUiState,
            downloadModelUiState = downloadModelUiState,
            onDownloadModel = screenModel::downloadModel,
            onRemoveModel = screenModel::removeModel,
        )
    }

    @Composable
    private fun Screen(
        modifier: Modifier = Modifier,
        modelListUiState: ModelListUiState,
        downloadModelUiState: DownloadModelUiState,
        onDownloadModel: (model: ModelLibrary) -> Unit,
        onRemoveModel: (model: ModelLibrary) -> Unit,
    ) {
        Navigator(ChatScreen()) {
            Scaffold(
                modifier = modifier,
                topBar = {
                    AppTopBar(
                        modelListUiState = modelListUiState,
                        downloadModelUiState = downloadModelUiState,
                        onDownloadModel = onDownloadModel,
                        onRemoveModel = onRemoveModel,
                    )
                },
                content = { CurrentScreen() },
            )
        }
    }

    @Composable
    private fun AppTopBar(
        modifier: Modifier = Modifier,
        modelListUiState: ModelListUiState,
        downloadModelUiState: DownloadModelUiState,
        onDownloadModel: (model: ModelLibrary) -> Unit,
        onRemoveModel: (model: ModelLibrary) -> Unit,
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
            ThemeSwitch(modifier = Modifier.padding(end = 8.dp).size(32.dp))

            ModelSelect(
                modifier = Modifier.height(32.dp),
                modelListUiState = modelListUiState,
                onDownloadModel = onDownloadModel,
                downloadModelUiState = downloadModelUiState,
                onRemoveModel = onRemoveModel,
            )

        }
    }
}
