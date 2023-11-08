package presentation.screen.chat

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.model.Model
import domain.model.ResultOf
import domain.repository.OllamaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatScreenModel(
    private val ollamaRepository: OllamaRepository,
): ScreenModel {
    private val _modelListUiState = MutableStateFlow<ModelListUiState>(ModelListUiState.Init)
    val modelListUiState: StateFlow<ModelListUiState> = _modelListUiState.asStateFlow()

    fun updateModelList() {
        screenModelScope.launch(Dispatchers.IO) {
            _modelListUiState.update{
                when (val result = ollamaRepository.listModels()) {
                    is ResultOf.Loading -> ModelListUiState.Loading
                    is ResultOf.Success -> ModelListUiState.Success(result.value)
                    is ResultOf.Failure -> ModelListUiState.Error(result.exception)
                }
            }
        }
    }
}

sealed interface ModelListUiState {
    data class Success(val models: List<Model>): ModelListUiState
    data class Error(val exception: Exception?): ModelListUiState
    object Loading: ModelListUiState
    object Init: ModelListUiState
}
