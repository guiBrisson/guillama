package data.repository

import model.Model
import model.ModelLibrary
import model.Models
import model.ResultOf
import domain.repository.OllamaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.logging.Logger

class OllamaRepositoryImpl : OllamaRepository, BaseOkHttpClientRepository() {
    private val logger = Logger.getLogger(this::class.java.simpleName)
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun listModels(): ResultOf<List<Model>> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url("$BASE_URL/tags").build()
        return@withContext baseCall(request) { jsonResponse ->
            if (jsonResponse != null) {
                val models = json.decodeFromString<Models>(jsonResponse)
                logger.info("fetched models: " + models.models.toString())
                models.models
            } else {
                emptyList()
            }
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

    override suspend fun removeModel(model: ModelLibrary): ResultOf<Unit> = withContext(Dispatchers.IO) {
        val body = mapOf("name" to model.modelName)
        val requestBody = json.encodeToJsonElement(body).toString().toRequestBody()

        val request = Request.Builder().url("$BASE_URL/delete").delete(requestBody).build()

        return@withContext baseCall(request) {
            logger.info("model ${model.modelName} removed")
        }
    }
}
