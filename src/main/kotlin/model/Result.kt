package model


sealed interface Result<out T> {
    data class Success<out R>(val value: R): Result<R>
    data class Failure(val exception: Exception): Result<Nothing>
}
