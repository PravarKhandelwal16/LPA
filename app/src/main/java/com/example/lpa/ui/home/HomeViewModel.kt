package com.example.lpa.ui.home

import com.example.lpa.core.base.BaseViewModel
import com.example.lpa.core.result.Result
import com.example.lpa.domain.usecase.GetDeviceStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the [HomeScreen].
 *
 * Responsibilities:
 * - Fetches device status from [GetDeviceStatusUseCase] on initialization.
 * - Exposes the result as [HomeUiState] via [uiState] (inherited from [BaseViewModel]).
 * - Provides [refresh] for pull-to-refresh / retry actions.
 *
 * All coroutines are launched with the inherited [launch] wrapper, which routes
 * uncaught exceptions to [onError] → [uiEvent] automatically.
 *
 * @param getDeviceStatusUseCase Injected by Hilt — retrieves device eSIM status.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDeviceStatusUseCase: GetDeviceStatusUseCase
) : BaseViewModel<HomeUiState>(initialState = HomeUiState.Loading) {

    init {
        loadStatus()
    }

    // ── Public Actions ────────────────────────────────────────────────────────

    /**
     * Triggers a fresh fetch of the device status.
     * Resets state to [HomeUiState.Loading] before the new request.
     */
    fun refresh() {
        setState(HomeUiState.Loading)
        loadStatus()
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private fun loadStatus() = launch {
        val result = getDeviceStatusUseCase()
        val newState = when (result) {
            is Result.Success -> HomeUiState.Success(result.data)
            is Result.Error   -> HomeUiState.Error(
                result.exception.message ?: "Failed to load device status"
            )
            Result.Loading    -> HomeUiState.Loading
        }
        setState(newState)
    }
}
