package domain.model


sealed interface ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>
    data class Loading(val status: String?, val progress: Float?): ResultOf<Nothing>
    data class Failure(val exception: Exception?): ResultOf<Nothing>
}
