package data.repository

import domain.repository.OllamaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import model.*
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class OllamaRepositoryImpl : OllamaRepository, BaseOkHttpClientRepository() {
    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)
    private val json = Json { ignoreUnknownKeys = true }

    private val serverProcessBuilder = ProcessBuilder("ollama", "serve")
    private var serverProcess: Process? = null

    override suspend fun listModels(): ResultOf<List<Model>> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url("$BASE_URL/tags").build()
        return@withContext baseCall(request) { jsonResponse ->
            if (jsonResponse != null) {
                val models = json.decodeFromString<Models>(jsonResponse)
                logger.info("fetched ${models.models.size} model(s)")
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

    override suspend fun serverRunning(): ResultOf<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val alive = serverProcess?.isAlive ?: false
            ResultOf.Success(alive)
        } catch (e: Exception) {
            e.printStackTrace()
            ResultOf.Failure(e)
        }
    }

    override suspend fun startServer() {
        withContext(Dispatchers.IO) {
            try {
                logger.info("running ollama server")
                serverProcess = serverProcessBuilder.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun stopServer() {
        withContext(Dispatchers.IO) {
            try {
                serverProcess?.let { process ->
                    if (process.isAlive) {
                        process.destroy()
                        val exitCode = process.waitFor()
                        logger.info("ollama server process finished with code $exitCode ")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun generateCompletion(modelName: String, prompt: String): Flow<ResultOf<Completion>> = flow {
        val body = """{"model": "$modelName", "prompt": "$prompt"}"""
        val command = arrayListOf("curl", "-X", "POST", "http://localhost:11434/api/generate", "-d", body)

        logger.info("running command: `\$ ${command.joinToString(" ")}`")
        emit(ResultOf.Loading(null, null))
        try {
            val process = ProcessBuilder(command).redirectOutput(ProcessBuilder.Redirect.PIPE).start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                val completion = json.decodeFromString<Completion>(line)
                emit(ResultOf.Success(completion))
                line = reader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResultOf.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
