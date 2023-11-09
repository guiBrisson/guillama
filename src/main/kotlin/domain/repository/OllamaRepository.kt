package domain.repository

import model.Model
import model.ModelLibrary
import model.ResultOf
import kotlinx.coroutines.flow.Flow

interface OllamaRepository {
    suspend fun listModels(): ResultOf<List<Model>>
    fun pullModel(model: ModelLibrary): Flow<ResultOf<Unit>>
    suspend fun removeModel(model: ModelLibrary): ResultOf<Unit>
}