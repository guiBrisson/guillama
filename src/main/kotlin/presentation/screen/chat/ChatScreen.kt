package presentation.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

class ChatScreen(
    private val modifier: Modifier = Modifier,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ChatScreenModel>()
        val modelListUiState by screenModel.modelListUiState.collectAsState()

        LaunchedEffect(modelListUiState) {
            if (modelListUiState == ModelListUiState.Init) {
                screenModel.updateModelList()
            }
        }

        Screen(
            modifier = modifier,
            modelListUiState = modelListUiState,
        )
    }

    @Composable
    private fun Screen(
        modifier: Modifier = Modifier,
        modelListUiState: ModelListUiState,
    ) {
        Column(modifier = modifier) {
            when(modelListUiState) {
                ModelListUiState.Loading -> CircularProgressIndicator()
                is ModelListUiState.Success -> {
                    for (model in modelListUiState.models) {
                        Text(model.name)
                    }
                }
                is ModelListUiState.Error -> Text(modelListUiState.exception.toString(), color = Color.Red)
                else -> Unit
            }
        }
    }

}
