package com.example.lpa.ui.profiles

import com.example.lpa.domain.models.EsimProfile

/**
 * Represents every possible state the Profiles screen can be in.
 *
 * @see ProfilesViewModel
 */
sealed interface ProfilesUiState {

    /** Initial state while profiles are being loaded. */
    data object Loading : ProfilesUiState

    /**
     * Profile list is empty — no profiles installed on the eUICC.
     *
     * Separate from [Success] with an empty list so the UI can show a
     * dedicated empty-state illustration rather than a blank list.
     */
    data object Empty : ProfilesUiState

    /**
     * Profiles loaded successfully.
     *
     * @param profiles       The current list of installed [EsimProfile]s.
     * @param isRefreshing   True while a background refresh is in progress
     *                       (allows showing a loading indicator on top of existing data).
     */
    data class Success(
        val profiles: List<EsimProfile>,
        val isRefreshing: Boolean = false
    ) : ProfilesUiState

    /**
     * Failed to load or refresh profiles.
     *
     * @param message Human-readable error description.
     */
    data class Error(val message: String) : ProfilesUiState
}
