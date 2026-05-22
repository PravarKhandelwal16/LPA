package com.example.lpa.core.result

/**
 * A generic wrapper for all operations in PrismEsimLpa that can succeed or fail.
 *
 * Using a sealed interface instead of a class allows exhaustive `when` expressions
 * without an `else` branch, giving the compiler the ability to warn on missing cases.
 *
 * Usage pattern in a repository:
 * ```kotlin
 * override suspend fun getProfiles(): Result<List<EsimProfile>> = try {
 *     Result.Success(localDataSource.getProfiles())
 * } catch (e: Exception) {
 *     Result.Error(e)
 * }
 * ```
 *
 * Usage pattern in a ViewModel:
 * ```kotlin
 * when (val result = getProfilesUseCase()) {
 *     is Result.Success -> { /* render result.data */ }
 *     is Result.Error   -> { /* render result.exception.message */ }
 *     Result.Loading    -> { /* render progress */ }
 * }
 * ```
 *
 * @param T The type of data carried on a successful result.
 */
sealed interface Result<out T> {

    /** The operation succeeded and produced [data]. */
    data class Success<T>(val data: T) : Result<T>

    /**
     * The operation failed with an [exception].
     *
     * @param exception The cause of the failure. Never null — prefer specific
     *                  exception types (e.g. [IOException], [EuiccException])
     *                  over a generic [Exception] for easier catch handling.
     */
    data class Error(val exception: Throwable) : Result<Nothing>

    /** The operation is in progress. No data available yet. */
    data object Loading : Result<Nothing>
}

// ─── Extension Helpers ────────────────────────────────────────────────────────

/** Returns the encapsulated data if this is [Result.Success], otherwise `null`. */
fun <T> Result<T>.getOrNull(): T? = (this as? Result.Success)?.data

/** Returns the encapsulated data if [Result.Success], or [default] otherwise. */
fun <T> Result<T>.getOrElse(default: T): T = (this as? Result.Success)?.data ?: default

/** Returns `true` if this is [Result.Success]. */
val Result<Any?>.isSuccess: Boolean get() = this is Result.Success

/** Returns `true` if this is [Result.Error]. */
val Result<Any?>.isError: Boolean get() = this is Result.Error

/** Returns `true` if this is [Result.Loading]. */
val Result<Any?>.isLoading: Boolean get() = this is Result.Loading

/**
 * Transforms the [Result.Success] data with [transform].
 * Passes [Result.Error] and [Result.Loading] through unchanged.
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error   -> this
    Result.Loading    -> Result.Loading
}
