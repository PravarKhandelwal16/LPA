package com.example.lpa.data.repository

import com.example.lpa.core.result.Result
import com.example.lpa.data.local.entity.LpaLogEntryEntity
import com.example.lpa.data.local.dao.LpaLogEntryDao
import com.example.lpa.data.mapper.toDomainList
import com.example.lpa.domain.models.LogLevel
import com.example.lpa.domain.models.LpaLogEntry
import com.example.lpa.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Production implementation of [LogRepository] backed by Room.
 *
 * @param logDao  Hilt-injected Room DAO for log entry persistence.
 */
class LogRepositoryImpl @Inject constructor(
    private val logDao: LpaLogEntryDao
) : LogRepository {

    /**
     * Returns a Room-backed [Flow] of domain [LpaLogEntry] objects, newest-first.
     *
     * Re-emits automatically whenever the `lpa_logs` table is modified —
     * new entries written by background services appear in the UI without polling.
     */
    override fun observeLogs(): Flow<List<LpaLogEntry>> =
        logDao.observeAll().map { entities -> entities.toDomainList() }

    /**
     * Deletes all log entries older than [retentionDays] days.
     *
     * Calculates the cutoff timestamp as `now - retentionDays` and delegates
     * to [LpaLogEntryDao.deleteOlderThan] for a single efficient DELETE query.
     *
     * @return [Result.Success] with the count of deleted rows, or [Result.Error].
     */
    override suspend fun purgeLogs(retentionDays: Int): Result<Unit> {
        return try {
            val cutoffMillis = System.currentTimeMillis() -
                TimeUnit.DAYS.toMillis(retentionDays.toLong())
            logDao.deleteOlderThan(cutoffMillis)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addLog(operation: String, level: LogLevel, detail: String?): Result<Unit> {
        return try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).format(Date())
            val entity = LpaLogEntryEntity(
                timestamp = timestamp,
                operation = operation,
                level = level.name,
                detail = detail
            )
            logDao.insert(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
