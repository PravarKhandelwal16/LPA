package com.example.lpa.data.datasource

import com.example.lpa.data.local.entity.EsimProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Contract for the **local** eSIM profile data source.
 *
 * Abstracts the Room DAO so the repository is testable without a real database.
 * Implemented by [com.example.lpa.data.datasource.LocalEsimProfileDataSource].
 *
 * ## Clean Architecture boundary
 * The repository only ever calls this interface — never [com.example.lpa.data.local.dao.EsimProfileDao]
 * directly. This allows swapping Room for a different persistence mechanism
 * without touching the repository.
 */
interface EsimProfileLocalDataSource {

    /** Reactive stream of all stored profiles, newest-first. */
    fun observeAll(): Flow<List<EsimProfileEntity>>

    /** Returns all profiles as a one-shot list. */
    suspend fun getAll(): List<EsimProfileEntity>

    /** Looks up a profile by ICCID. Returns `null` if not found. */
    suspend fun findByIccId(iccId: String): EsimProfileEntity?

    /** Persists a single profile. Returns the new row ID. */
    suspend fun insert(profile: EsimProfileEntity): Long

    /**
     * Replaces all stored profiles with [profiles] in a single transaction.
     * Used after a full eUICC refresh to keep the DB in sync.
     */
    suspend fun replaceAll(profiles: List<EsimProfileEntity>)

    /** Deactivates all profiles then marks [id] as active. */
    suspend fun setActive(id: Long)

    /** Removes a specific profile record. */
    suspend fun delete(profile: EsimProfileEntity)

    /** Removes all profile records. */
    suspend fun deleteAll()
}
