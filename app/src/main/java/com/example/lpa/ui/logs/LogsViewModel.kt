package com.example.lpa.ui.logs

import androidx.lifecycle.viewModelScope
import com.example.lpa.core.base.BaseViewModel
import com.example.lpa.core.base.UiEvent
import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.LogLevel
import com.example.lpa.domain.usecase.ObserveLogsUseCase
import com.example.lpa.domain.usecase.PurgeOldLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * ViewModel for the [LogsScreen].
 *
 * Responsibilities:
 * - Collects the live [LogRepository.observeLogs] [kotlinx.coroutines.flow.Flow].
 * - Maintains an active [LogLevel] filter applied in-memory (no DB query needed).
 * - Exposes [setFilter] and [clearFilter] for the filter chip UI.
 * - Exposes [purgeLogs] for the "Clear logs" action.
 *
 * @param observeLogsUseCase Injected by Hilt — observes LPA logs.
 * @param purgeOldLogsUseCase Injected by Hilt — purges old logs.
 */
@HiltViewModel
class LogsViewModel @Inject constructor(
    private val observeLogsUseCase: ObserveLogsUseCase,
    private val purgeOldLogsUseCase: PurgeOldLogsUseCase
) : BaseViewModel<LogsUiState>(initialState = LogsUiState.Loading) {

    init {
        observeLogs()
    }

    // ── Public Actions ────────────────────────────────────────────────────────

    /**
     * Applies a [LogLevel] filter to the displayed log entries.
     * The full list is retained in state so switching filters is instant.
     */
    fun setFilter(level: LogLevel) {
        val current = uiState.value as? LogsUiState.Success ?: return
        setState(
            current.copy(
                activeFilter = level,
                filteredEntries = current.entries.filter { it.level == level }
            )
        )
    }

    /**
     * Clears any active filter and shows all log entries.
     */
    fun clearFilter() {
        val current = uiState.value as? LogsUiState.Success ?: return
        setState(
            current.copy(
                activeFilter = null,
                filteredEntries = current.entries
            )
        )
    }

    /**
     * Purges all log entries older than [retentionDays] days.
     *
     * @param retentionDays Number of days to keep. Entries older than this are deleted.
     */
    fun purgeLogs(retentionDays: Int = 30) = launch {
        val result = purgeOldLogsUseCase(retentionDays)
        if (result is Result.Error) {
            sendEvent(UiEvent.ShowError(result.exception.message ?: "Failed to purge logs"))
        } else {
            sendEvent(UiEvent.ShowMessage("Logs older than $retentionDays days cleared"))
        }
        // observeLogs() will emit the updated list automatically
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private fun observeLogs() {
        observeLogsUseCase()
            .onEach { entries ->
                val current = uiState.value
                val activeFilter = (current as? LogsUiState.Success)?.activeFilter

                val newState = when {
                    entries.isEmpty() -> LogsUiState.Empty
                    activeFilter != null -> LogsUiState.Success(
                        entries = entries,
                        activeFilter = activeFilter,
                        filteredEntries = entries.filter { it.level == activeFilter }
                    )
                    else -> LogsUiState.Success(entries = entries)
                }
                setState(newState)
            }
            .launchIn(viewModelScope)
    }
}
