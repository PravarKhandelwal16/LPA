package com.example.lpa.ui.profiles

import androidx.lifecycle.viewModelScope
import com.example.lpa.core.base.BaseViewModel
import com.example.lpa.core.base.UiEvent
import com.example.lpa.core.result.Result
import com.example.lpa.domain.usecase.ObserveProfilesUseCase
import com.example.lpa.domain.usecase.RefreshProfilesUseCase
import com.example.lpa.domain.usecase.SwitchProfileUseCase
import com.example.lpa.domain.usecase.ToggleProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * ViewModel for the [ProfilesScreen].
 *
 * Responsibilities:
 * - Collects the live [ProfileRepository.observeProfiles] [kotlinx.coroutines.flow.Flow]
 *   and maps it to [ProfilesUiState] — the UI reacts automatically to eUICC changes.
 * - Exposes [refresh] to trigger a manual pull-to-refresh from the eUICC.
 *
 * @param observeProfilesUseCase Injected by Hilt — observes eSIM profiles.
 * @param refreshProfilesUseCase Injected by Hilt — refreshes eSIM profiles.
 */
@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val observeProfilesUseCase: ObserveProfilesUseCase,
    private val refreshProfilesUseCase: RefreshProfilesUseCase,
    private val switchProfileUseCase: SwitchProfileUseCase,
    private val toggleProfileUseCase: ToggleProfileUseCase
) : BaseViewModel<ProfilesUiState>(initialState = ProfilesUiState.Loading) {

    init {
        observeProfiles()
        refresh()
    }

    // ── Public Actions ────────────────────────────────────────────────────────

    /**
     * Triggers a refresh of profile data from the eUICC.
     *
     * Shows a refreshing indicator while in progress ([ProfilesUiState.Success.isRefreshing]),
     * then reverts to [ProfilesUiState.Success] or [ProfilesUiState.Error].
     */
    fun refresh() {
        // Show refreshing indicator on top of existing data if available
        val current = uiState.value
        if (current is ProfilesUiState.Success) {
            setState(current.copy(isRefreshing = true))
        }
        launch {
            val result = refreshProfilesUseCase()
            if (result is Result.Error) {
                sendEvent(
                    UiEvent.ShowError(
                        result.exception.message ?: "Failed to refresh profiles"
                    )
                )
                // Reset refreshing flag if still in Success state
                val afterRefresh = uiState.value
                if (afterRefresh is ProfilesUiState.Success) {
                    setState(afterRefresh.copy(isRefreshing = false))
                }
            }
            // On success: observeProfiles() will emit the updated list automatically
        }
    }

    /**
     * Toggles the state of a specific eSIM profile.
     */
    fun toggleProfile(iccId: String, enable: Boolean) {
        val current = uiState.value
        if (current is ProfilesUiState.Success) {
            setState(current.copy(isRefreshing = true))
        }
        launch {
            val result = toggleProfileUseCase(iccId, enable)
            if (result is Result.Error) {
                sendEvent(
                    UiEvent.ShowError(
                        result.exception.message ?: "Failed to toggle profile"
                    )
                )
            }
            // Reset refreshing flag regardless of outcome
            val afterToggle = uiState.value
            if (afterToggle is ProfilesUiState.Success) {
                setState(afterToggle.copy(isRefreshing = false))
            }
        }
    }

    // ── Private ───────────────────────────────────────────────────────────────

    /**
     * Subscribes to the profile [kotlinx.coroutines.flow.Flow] from the use case.
     * Converts each emission into the appropriate [ProfilesUiState].
     */
    private fun observeProfiles() {
        observeProfilesUseCase()
            .onEach { profiles ->
                val newState = if (profiles.isEmpty()) {
                    ProfilesUiState.Empty
                } else {
                    ProfilesUiState.Success(profiles = profiles)
                }
                setState(newState)
            }
            .launchIn(viewModelScope)
    }
}
