package com.example.lpa.ui.settings

import com.example.lpa.domain.models.AppSettings

/**
 * Represents every possible state the Settings screen can be in.
 *
 * @see SettingsViewModel
 */
sealed interface SettingsUiState {

    /** Initial state while persisted settings are being loaded. */
    data object Loading : SettingsUiState

    /**
     * Settings loaded and ready to display.
     *
     * @param settings     The current [AppSettings] to render.
     * @param isSaving     True while a save operation is in flight — disables the save button.
     */
    data class Success(
        val settings: AppSettings,
        val isSaving: Boolean = false
    ) : SettingsUiState

    /**
     * Failed to load or save settings.
     *
     * @param message Human-readable error description.
     */
    data class Error(val message: String) : SettingsUiState
}
