package com.example.lpa.domain.repository

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.LpaLogEntry
import kotlinx.coroutines.flow.Flow

/**
 * Contract for reading and managing LPA operation audit logs.
 *
 * Implemented in the **data** layer ([com.example.lpa.data.repository.LogRepositoryImpl]).
 */
interface LogRepository {

    /**
     * A live stream of all stored [LpaLogEntry] items, ordered newest-first.
     *
     * Backed by a Room [Flow] — the UI updates automatically when new log
     * entries are written by background services.
     */
    fun observeLogs(): Flow<List<LpaLogEntry>>

    /**
     * Deletes all log entries older than [retentionDays] days.
     *
     * @param retentionDays Number of days to retain.
     * @return [Result.Success] on success, [Result.Error] on failure.
     */
    suspend fun purgeLogs(retentionDays: Int): Result<Unit>
}
