package data.repository

import domain.model.Model
import domain.model.ModelLibrary
import domain.model.Models
import domain.model.ResultOf
import domain.repository.OllamaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Request
import java.util.logging.Logger

class OllamaRepositoryImpl : OllamaRepository, BaseOkHttpClientRepository() {
    private val logger = Logger.getLogger(this::class.java.simpleName)
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun listModels(): ResultOf<List<Model>> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url("$BASE_URL/tags").build()
        return@withContext baseCall(request) {
            val models = json.decodeFromString<Models>(it)
            logger.info("fetched models: " + models.models.toString())
            models.models
        }
    }

    /**
     * To pull the model this function runs a curl command.
     * Todo: send back all the inputs from the process builder in form of ResultOf<>
     **/
    override fun pullModel(model: ModelLibrary) = flow {
        val body = """{"name": "${model.modelName}:latest"}"""
        val command = arrayListOf("curl", "-X", "POST", "http://localhost:11434/api/pull", "-d", body)

        logger.info("running command: `\$ ${command.joinToString(" ")}`")

        try {
            val process = ProcessBuilder(command).inheritIO().start()
            emit(ResultOf.Loading(null, null))
            process.waitFor()
            logger.info("command success")
            emit(ResultOf.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResultOf.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
