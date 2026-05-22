package com.example.lpa.core.result

/**
 * Executes [onSuccess] if this is [Result.Success], [onError] if [Result.Error].
 * Returns `this` unchanged so calls can be chained.
 *
 * Usage:
 * ```kotlin
 * repository.getProfiles()
 *     .onSuccess { profiles -> setState(ProfilesUiState.Success(profiles)) }
 *     .onError   { e       -> setState(ProfilesUiState.Error(e.message.orEmpty())) }
 * ```
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

/**
 * Executes [action] with the exception if this is [Result.Error].
 * Returns `this` unchanged.
 */
inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

/**
 * Executes [action] if this is [Result.Loading].
 * Returns `this` unchanged.
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}

/**
 * Returns the encapsulated data when [Result.Success], or the result of
 * [onError] when [Result.Error], or the result of [onLoading] when loading.
 *
 * Useful for converting a [Result] into a single value at a terminal call site:
 * ```kotlin
 * val name = result.fold(
 *     onSuccess = { it.nickname },
 *     onError   = { "Unknown" },
 *     onLoading = { "Loading…" }
 * )
 * ```
 */
inline fun <T, R> Result<T>.fold(
    onSuccess: (T) -> R,
    onError: (Throwable) -> R,
    onLoading: () -> R = { throw IllegalStateException("Unexpected Loading state") }
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error   -> onError(exception)
    Result.Loading    -> onLoading()
}

/**
 * Wraps a suspending [block] in a try/catch and returns [Result.Success] on
 * normal completion or [Result.Error] if any [Exception] is thrown.
 *
 * Intended for use inside Repository `suspend` functions to eliminate
 * repetitive try/catch boilerplate:
 * ```kotlin
 * override suspend fun getDeviceStatus() = resultOf {
 *     euiccManagerWrapper.getDeviceStatus()
 * }
 * ```
 */
suspend fun <T> resultOf(block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Exception) {
    Result.Error(e)
}
