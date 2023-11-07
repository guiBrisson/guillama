package network

import kotlinx.serialization.json.Json
import model.Models
import model.Result
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OllamaRepository {
    private val client = OkHttpClient()
    private val json = Json

    suspend fun listModels(): Result<Models> {
        val request = Request.Builder().url("$BASE_URL/tags").build()
        return baseCall(request) { json.decodeFromString<Models>(it) }
    }

    private suspend fun <T> baseCall(request: Request, action: (jsonResponse: String) -> T): Result<T> =
        suspendCoroutine { cont ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    cont.resume(Result.Failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        cont.resume(Result.Failure(IOException("Unexpected code $response")))
                    }

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    cont.resume(Result.Success(action(response.body!!.string())))
                }
            })
        }

    companion object {
        private const val BASE_URL = "http://localhost:11434/api"
    }
}
