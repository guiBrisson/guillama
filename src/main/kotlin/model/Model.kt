package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Model(
    val name: String,
    @SerialName("modified_at") val modifiedAt: String,
    val size: Long,
    val digest: String,
)

@Serializable
data class Models(
    val models: List<Model>
)
