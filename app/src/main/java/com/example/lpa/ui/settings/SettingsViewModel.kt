package com.example.lpa.ui.settings

import androidx.lifecycle.viewModelScope
import com.example.lpa.core.base.BaseViewModel
import com.example.lpa.core.base.UiEvent
import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.AppSettings
import com.example.lpa.domain.usecase.ObserveSettingsUseCase
import com.example.lpa.domain.usecase.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * ViewModel for the [SettingsScreen].
 *
 * Responsibilities:
 * - Observes [SettingsRepository.observeSettings] and exposes settings via [SettingsUiState].
 * - Exposes individual update functions for each setting field so composables
 *   can call them directly from UI callbacks.
 * - Handles the save operation with an [SettingsUiState.Success.isSaving] indicator.
 *
 * @param observeSettingsUseCase Injected by Hilt — observes [AppSettings].
 * @param saveSettingsUseCase Injected by Hilt — saves [AppSettings].
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : BaseViewModel<SettingsUiState>(initialState = SettingsUiState.Loading) {

    init {
        observeSettings()
    }

    // ── Public Actions ────────────────────────────────────────────────────────

    /**
     * Updates the SM-DP+ server URL setting.
     * Saves immediately — no explicit "save" button needed for URL changes.
     */
    fun onSmdpServerUrlChange(url: String) {
        updateCurrentSettings { it.copy(smdpServerUrl = url) }
    }

    /**
     * Toggles the dark theme preference and saves immediately.
     */
    fun onDarkThemeToggle(enabled: Boolean) {
        updateCurrentSettings { it.copy(isDarkThemeEnabled = enabled) }
    }

    /**
     * Toggles the certificate pinning security setting and saves immediately.
     */
    fun onCertificatePinningToggle(enabled: Boolean) {
        updateCurrentSettings { it.copy(isCertificatePinningEnabled = enabled) }
    }

    /**
     * Updates the log retention period and saves immediately.
     */
    fun onLogRetentionDaysChange(days: Int) {
        updateCurrentSettings { it.copy(logRetentionDays = days) }
    }

    /**
     * Explicitly saves the current in-progress [AppSettings] to persistent storage.
     *
     * Use this for a dedicated "Save" / "Apply" button if the UI batches changes.
     */
    fun saveSettings() {
        val current = uiState.value as? SettingsUiState.Success ?: return
        launch {
            setState(current.copy(isSaving = true))
            val result = saveSettingsUseCase(current.settings)
            setState(current.copy(isSaving = false))

            when (result) {
                is Result.Success -> sendEvent(UiEvent.ShowMessage("Settings saved"))
                is Result.Error   -> sendEvent(
                    UiEvent.ShowError(result.exception.message ?: "Failed to save settings")
                )
                Result.Loading    -> Unit
            }
        }
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private fun observeSettings() {
        observeSettingsUseCase()
            .onEach { settings ->
                setState(SettingsUiState.Success(settings = settings))
            }
            .launchIn(viewModelScope)
    }

    /**
     * Applies [transform] to the current [AppSettings] and persists the result.
     * No-op if the current state is not [SettingsUiState.Success].
     */
    private fun updateCurrentSettings(transform: (AppSettings) -> AppSettings) {
        val current = uiState.value as? SettingsUiState.Success ?: return
        val updated = transform(current.settings)
        setState(current.copy(settings = updated))
        launch {
            val result = saveSettingsUseCase(updated)
            if (result is Result.Error) {
                sendEvent(UiEvent.ShowError(result.exception.message ?: "Failed to save setting"))
                // Roll back the optimistic UI update
                setState(current)
            }
        }
    }
}
