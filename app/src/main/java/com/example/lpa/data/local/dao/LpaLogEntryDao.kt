package com.example.lpa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lpa.data.local.entity.LpaLogEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object for [LpaLogEntryEntity].
 *
 * Provides Flow-based reactive queries and coroutine-safe writes for the
 * `lpa_logs` table. All writes are `suspend`; reads returning [Flow] are non-suspend.
 *
 * Only called from [com.example.lpa.data.repository.LogRepositoryImpl].
 */
@Dao
interface LpaLogEntryDao {

    // ── Observe (Flow) ────────────────────────────────────────────────────────

    /**
     * Emits the full log list ordered newest-first.
     * Re-emits whenever any row in `lpa_logs` is inserted or deleted.
     */
    @Query("SELECT * FROM lpa_logs ORDER BY created_at DESC")
    fun observeAll(): Flow<List<LpaLogEntryEntity>>

    /**
     * Emits only log entries with the given [level] (e.g. "ERROR"), newest-first.
     * Re-emits on any table change — the WHERE clause is evaluated in Room.
     */
    @Query("SELECT * FROM lpa_logs WHERE level = :level ORDER BY created_at DESC")
    fun observeByLevel(level: String): Flow<List<LpaLogEntryEntity>>

    // ── One-shot Queries ──────────────────────────────────────────────────────

    /**
     * Returns the total number of log entries currently stored.
     */
    @Query("SELECT COUNT(*) FROM lpa_logs")
    suspend fun count(): Int

    // ── Writes ────────────────────────────────────────────────────────────────

    /**
     * Inserts a single log entry.
     *
     * @return The row ID of the inserted entry.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: LpaLogEntryEntity): Long

    /**
     * Inserts multiple log entries in a single transaction.
     *
     * @return List of row IDs for the inserted entries.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entries: List<LpaLogEntryEntity>): List<Long>

    /**
     * Deletes all log entries whose [LpaLogEntryEntity.createdAt] is older than
     * [cutoffMillis] (Unix epoch millis). Used for log retention enforcement.
     *
     * @return The number of rows deleted.
     */
    @Query("DELETE FROM lpa_logs WHERE created_at < :cutoffMillis")
    suspend fun deleteOlderThan(cutoffMillis: Long): Int

    /**
     * Deletes all log entries unconditionally (e.g. on sign-out or purge action).
     */
    @Query("DELETE FROM lpa_logs")
    suspend fun deleteAll(): Int
}
