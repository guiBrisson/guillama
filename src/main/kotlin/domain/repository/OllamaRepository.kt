package domain.repository

import domain.model.Model
import domain.model.ModelLibrary
import domain.model.ResultOf
import kotlinx.coroutines.flow.Flow

interface OllamaRepository {
    suspend fun listModels(): ResultOf<List<Model>>
    fun pullModel(model: ModelLibrary): Flow<ResultOf<Unit>>
}