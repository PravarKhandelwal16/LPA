package com.example.lpa.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Abstract base class for all ViewModels in PrismEsimLpa.
 *
 * Provides a consistent pattern for:
 * - Typed UI state via [StateFlow] (rendered every recomposition that cares).
 * - One-time UI events via [SharedFlow] (toasts, navigation, dialogs).
 * - Centralized uncaught-exception handling routed to [onError].
 *
 * ## Pattern
 * ```kotlin
 * class HomeViewModel @Inject constructor(
 *     private val getStatusUseCase: GetDeviceStatusUseCase
 * ) : BaseViewModel<HomeUiState>(HomeUiState.Loading) {
 *
 *     init { loadStatus() }
 *
 *     private fun loadStatus() = launch {
 *         val result = getStatusUseCase()
 *         setState(HomeUiState.from(result))
 *     }
 * }
 * ```
 *
 * @param S  The UiState type this ViewModel manages.
 * @param initialState The starting value for [uiState].
 */
abstract class BaseViewModel<S>(initialState: S) : ViewModel() {

    // ── UI State ──────────────────────────────────────────────────────────────

    private val _uiState = MutableStateFlow(initialState)

    /**
     * The current UI state. Collect this in your composable via
     * `collectAsStateWithLifecycle()`.
     */
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // ── One-time UI Events ────────────────────────────────────────────────────

    private val _uiEvent = MutableSharedFlow<UiEvent>()

    /**
     * One-time events (e.g. show snackbar, navigate, open dialog).
     * Collect this in your composable via `LaunchedEffect`.
     *
     * Uses a [SharedFlow] with no replay so that events are not re-delivered
     * after configuration changes — preventing duplicate toasts / navigations.
     */
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    // ── Coroutine Error Handler ───────────────────────────────────────────────

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    // ── Protected API ─────────────────────────────────────────────────────────

    /**
     * Updates the current UI state.
     *
     * Prefer calling [setState] over exposing [MutableStateFlow] directly,
     * keeping mutation centralised and easy to trace.
     */
    protected fun setState(newState: S) {
        _uiState.value = newState
    }

    /**
     * Updates the current UI state using a transform on the current value.
     * Useful for partial state updates without replacing the whole object.
     */
    protected fun updateState(transform: (S) -> S) {
        _uiState.value = transform(_uiState.value)
    }

    /**
     * Emits a one-time [UiEvent] to the UI layer.
     * Call this from a coroutine or use [launch] below.
     */
    protected suspend fun sendEvent(event: UiEvent) {
        _uiEvent.emit(event)
    }

    /**
     * Launches a coroutine in [viewModelScope] with the shared
     * [exceptionHandler] so uncaught exceptions always route to [onError].
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(exceptionHandler, block = block)

    /**
     * Called when an uncaught exception escapes a [launch] block.
     *
     * Default implementation emits a [UiEvent.ShowError] event.
     * Override in subclasses for feature-specific recovery logic.
     */
    protected open fun onError(throwable: Throwable) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowError(throwable.message ?: "An unexpected error occurred"))
        }
    }
}

// ─── Common One-Time UI Events ────────────────────────────────────────────────

/**
 * Sealed hierarchy of one-time events that the UI layer should handle exactly once.
 *
 * Extend with feature-specific events by creating a local sealed interface
 * that delegates to these or adds new cases.
 */
sealed interface UiEvent {
    /** Show a Snackbar or Toast with [message]. */
    data class ShowError(val message: String) : UiEvent

    /** Show an informational Snackbar with [message]. */
    data class ShowMessage(val message: String) : UiEvent

    /** Navigate to a route. */
    data class Navigate(val route: String) : UiEvent

    /** Navigate back in the back stack. */
    data object NavigateBack : UiEvent
}
