package com.example.lpa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lpa.data.local.entity.EsimProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object for [EsimProfileEntity].
 *
 * All write operations are `suspend` functions — they must be called from
 * a coroutine. All read operations that return [Flow] are non-suspend;
 * Room handles the coroutine context internally and emits new values
 * whenever the underlying table changes.
 *
 * The repository ([com.example.lpa.data.repository.ProfileRepositoryImpl])
 * is the only caller of this DAO — never call it directly from a ViewModel.
 */
@Dao
interface EsimProfileDao {

    // ── Observe (Flow) ────────────────────────────────────────────────────────

    /**
     * Emits the complete list of profiles ordered by creation date (newest first).
     * Re-emits automatically whenever any row in `esim_profiles` changes.
     */
    @Query("SELECT * FROM esim_profiles ORDER BY created_at DESC")
    fun observeAll(): Flow<List<EsimProfileEntity>>

    /**
     * Emits only the currently active profile, or `null` if none is active.
     * Re-emits when the active profile changes.
     */
    @Query("SELECT * FROM esim_profiles WHERE is_active = 1 LIMIT 1")
    fun observeActiveProfile(): Flow<EsimProfileEntity?>

    /**
     * Emits a single profile by [id], or `null` if not found.
     */
    @Query("SELECT * FROM esim_profiles WHERE id = :id")
    fun observeById(id: Long): Flow<EsimProfileEntity?>

    // ── One-shot Queries ──────────────────────────────────────────────────────

    /**
     * Returns all profiles as a plain list (non-reactive, for one-shot reads).
     */
    @Query("SELECT * FROM esim_profiles ORDER BY created_at DESC")
    suspend fun getAll(): List<EsimProfileEntity>

    /**
     * Returns the total number of stored profiles.
     */
    @Query("SELECT COUNT(*) FROM esim_profiles")
    suspend fun count(): Int

    /**
     * Looks up a profile by its ICCID. Returns `null` if not found.
     */
    @Query("SELECT * FROM esim_profiles WHERE icc_id = :iccId LIMIT 1")
    suspend fun findByIccId(iccId: String): EsimProfileEntity?

    // ── Writes ────────────────────────────────────────────────────────────────

    /**
     * Inserts a new profile. If a row with the same primary key already exists,
     * it is replaced (upsert behaviour).
     *
     * @return The row ID of the newly inserted row.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: EsimProfileEntity): Long

    /**
     * Inserts multiple profiles in a single transaction.
     * Existing rows with the same primary key are replaced.
     *
     * @return List of row IDs for the inserted rows.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(profiles: List<EsimProfileEntity>): List<Long>

    /**
     * Updates an existing profile row.
     * The [EsimProfileEntity.id] field is used to locate the row.
     * Returns the number of rows updated.
     */
    @Update
    suspend fun update(profile: EsimProfileEntity): Int

    /**
     * Deactivates all profiles by setting `is_active = 0`.
     * Call this before activating a new profile to ensure only one is active.
     */
    @Query("UPDATE esim_profiles SET is_active = 0")
    suspend fun deactivateAll(): Int

    /**
     * Sets the profile with [id] as active.
     * Call [deactivateAll] in the same transaction before calling this.
     */
    @Query("UPDATE esim_profiles SET is_active = 1, updated_at = :updatedAt WHERE id = :id")
    suspend fun setActive(id: Long, updatedAt: Long = System.currentTimeMillis()): Int

    /**
     * Deletes a specific profile row.
     */
    @Delete
    suspend fun delete(profile: EsimProfileEntity): Int

    /**
     * Deletes all profiles from the table (e.g. on sign-out or factory reset).
     */
    @Query("DELETE FROM esim_profiles")
    suspend fun deleteAll(): Int
}
