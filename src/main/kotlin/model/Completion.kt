package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Completion(
    val model: String,
    @SerialName("created_at") val createdAt: String,
    val response: String,
    val done: Boolean,
)
