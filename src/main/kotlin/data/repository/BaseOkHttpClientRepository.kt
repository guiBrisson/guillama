package data.repository

import model.ResultOf
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class BaseOkHttpClientRepository {
    private val client = OkHttpClient()

    suspend fun <T> baseCall(request: Request, action: (jsonResponse: String) -> T): ResultOf<T> =
        suspendCoroutine { cont ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    cont.resume(ResultOf.Failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        cont.resume(ResultOf.Failure(IOException("Unexpected code $response")))
                    }

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    cont.resume(ResultOf.Success(action(response.body!!.string())))
                }
            })
        }

    companion object {
        const val BASE_URL = "http://localhost:11434/api"
        private val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }
}