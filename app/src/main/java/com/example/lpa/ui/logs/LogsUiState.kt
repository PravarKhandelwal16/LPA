package com.example.lpa.ui.logs

import com.example.lpa.domain.models.LpaLogEntry
import com.example.lpa.domain.models.LogLevel

/**
 * Represents every possible state the Logs screen can be in.
 *
 * @see LogsViewModel
 */
sealed interface LogsUiState {

    /** Initial state while logs are being fetched from the database. */
    data object Loading : LogsUiState

    /** No log entries exist yet (fresh install or after purge). */
    data object Empty : LogsUiState

    /**
     * Log entries loaded successfully.
     *
     * @param entries          All [LpaLogEntry] items, newest-first.
     * @param activeFilter     The currently applied [LogLevel] filter, or null for all levels.
     * @param filteredEntries  Pre-filtered list ready for the UI to render directly.
     */
    data class Success(
        val entries: List<LpaLogEntry>,
        val activeFilter: LogLevel? = null,
        val filteredEntries: List<LpaLogEntry> = entries
    ) : LogsUiState

    /**
     * Failed to load logs.
     *
     * @param message Human-readable error description.
     */
    data class Error(val message: String) : LogsUiState
}
