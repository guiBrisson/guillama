package presentation.screen.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import model.ResultOf
import domain.repository.OllamaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import model.ModelLibrary

class MainScreenModel(
    private val ollamaRepository: OllamaRepository,
) : ScreenModel {
    private val _modelListUiState = MutableStateFlow<ModelListUiState>(ModelListUiState.Init)
    val modelListUiState: StateFlow<ModelListUiState> = _modelListUiState.asStateFlow()

    private val _downloadModelUiState = MutableStateFlow<DownloadModelUiState>(DownloadModelUiState.Idle)
    val downloadModelUiState: StateFlow<DownloadModelUiState> = _downloadModelUiState.asStateFlow()

    fun updateModelList() {
        screenModelScope.launch(Dispatchers.IO) {
            _modelListUiState.update {
                when (val result = ollamaRepository.listModels()) {
                    is ResultOf.Loading -> ModelListUiState.Loading
                    is ResultOf.Success -> ModelListUiState.Success(result.value)
                    is ResultOf.Failure -> ModelListUiState.Error(result.exception)
                }
            }
        }
    }

    fun downloadModel(model: ModelLibrary) {
        screenModelScope.launch(Dispatchers.IO) {
            ollamaRepository.pullModel(model).collect { result ->
                when (result) {
                    is ResultOf.Loading -> _downloadModelUiState.update { DownloadModelUiState.Downloading }
                    is ResultOf.Success -> {
                        _downloadModelUiState.update { DownloadModelUiState.Idle }
                        updateModelList()
                    }

                    is ResultOf.Failure -> _downloadModelUiState.update { DownloadModelUiState.Error(result.exception) }
                }
            }
        }
    }

    fun removeModel(model: ModelLibrary) {
        screenModelScope.launch(Dispatchers.IO) {
            when (ollamaRepository.removeModel(model)) {
                is ResultOf.Success -> updateModelList()
                else -> Unit
            }
        }
    }
}
