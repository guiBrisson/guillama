package presentation.screen.main

import model.Model

sealed interface ModelListUiState {
    data class Success(val models: List<Model>) : ModelListUiState
    data class Error(val exception: Exception?) : ModelListUiState
    object Loading : ModelListUiState
    object Init : ModelListUiState

    fun isSuccess(): Boolean {
        return when (this) {
            is Success -> true
            else -> false
        }
    }

    fun isLoading(): Boolean {
        return when (this) {
            is Loading -> true
            else -> false
        }
    }
}

sealed interface DownloadModelUiState {
    object Idle : DownloadModelUiState
    object Downloading : DownloadModelUiState
    data class Error(val exception: Exception?) : DownloadModelUiState

    fun isDownloading(): Boolean {
        return when (this) {
            is Downloading -> true
            else -> false
        }
    }
}

sealed interface ServerUiState {
    object Idle: ServerUiState
    object Running: ServerUiState
    data class Error(val exception: Exception?): ServerUiState

    fun isRunning(): Boolean {
        return when(this) {
            Running -> true
            else -> false
        }
    }
}