package domain.repository

import model.Model
import model.ModelLibrary
import model.ResultOf
import kotlinx.coroutines.flow.Flow
import model.Completion

interface OllamaRepository {
    suspend fun listModels(): ResultOf<List<Model>>
    fun pullModel(model: ModelLibrary): Flow<ResultOf<Unit>>
    suspend fun removeModel(model: ModelLibrary): ResultOf<Unit>
    suspend fun serverRunning(): ResultOf<Boolean>
    suspend fun startServer()
    suspend fun stopServer()
    suspend fun generateCompletion(modelName: String, prompt: String): Flow<ResultOf<Completion>>
}