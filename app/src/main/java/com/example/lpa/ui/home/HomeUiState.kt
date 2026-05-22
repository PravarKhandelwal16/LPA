package com.example.lpa.ui.home

import com.example.lpa.domain.models.DeviceStatus

/**
 * Represents every possible state the Home screen can be in.
 *
 * The ViewModel exposes a single [kotlinx.coroutines.flow.StateFlow]<[HomeUiState]>
 * and the composable renders whichever case is current.
 *
 * ## Why a sealed interface?
 * Exhaustive `when` expressions in Compose — the compiler forces handling all states,
 * preventing forgotten loading/error cases from shipping.
 */
sealed interface HomeUiState {

    /** Initial state while [DeviceStatus] is being fetched. */
    data object Loading : HomeUiState

    /**
     * Device status loaded successfully.
     *
     * @param status The current eSIM / eUICC device status.
     */
    data class Success(val status: DeviceStatus) : HomeUiState

    /**
     * The status fetch failed.
     *
     * @param message Human-readable error description to show in the UI.
     */
    data class Error(val message: String) : HomeUiState
}
