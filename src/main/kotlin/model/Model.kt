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

enum class ModelLibrary(val modelName: String, val size: Float, val parameters: String) {
    MISTRAL(modelName = "mistral", size = 4.1f, parameters = "7B"),
    LLAMA2(modelName = "llama2", size = 3.8f, parameters = "7B"),
    CODE_LLAMA(modelName = "codellama", size = 3.8f, parameters = "7B"),
    LLAMA2_UNCENSORED(modelName = "llama2-uncensored", size = 3.8f, parameters = "7B"),
    ORCA_MINI(modelName = "orca-mini", size = 1.9f, parameters = "3B"),
    VICUNA(modelName = "vicuna", size = 3.8f, parameters = "7B");

    companion object {
        fun fromModel(model: Model): ModelLibrary? {
            return enumValues<ModelLibrary>().find { model.name.contains(it.name, ignoreCase = true) }
        }
    }
}
