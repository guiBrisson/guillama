package presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import presentation.screen.chat.ChatScreen

class MainScreen(private val modifier: Modifier = Modifier): Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MainScreenModel>()
        val modelListUiState by screenModel.modelListUiState.collectAsState()

        LaunchedEffect(modelListUiState) {
            if (modelListUiState == ModelListUiState.Init) {
                screenModel.updateModelList()
            }
        }

        Screen(modifier = modifier, modelListUiState = modelListUiState)
    }

    @Composable
    private fun Screen(
        modifier: Modifier = Modifier,
        modelListUiState: ModelListUiState,
    ) {
        Navigator(ChatScreen()) {
            Scaffold(
                modifier = modifier,
                topBar = { AppTopBar(modelListUiState = modelListUiState) },
                content = { CurrentScreen() },
            )
        }
    }

    @Composable
    private fun AppTopBar(modifier: Modifier = Modifier, modelListUiState: ModelListUiState) {
        Row(modifier = modifier.fillMaxWidth().height(40.dp).background(Color.Gray)) {
            when(modelListUiState) {
                ModelListUiState.Loading -> CircularProgressIndicator()
                is ModelListUiState.Success -> {
                    for (model in modelListUiState.models) {
                        Text(model.name)
                    }
                }
                is ModelListUiState.Error -> Text(modelListUiState.exception.toString())
                else -> Unit
            }
        }
    }
}
