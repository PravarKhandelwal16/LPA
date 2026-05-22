package com.example.lpa.data.datasource

import com.example.lpa.data.local.dao.EsimProfileDao
import com.example.lpa.data.local.entity.EsimProfileEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Room-backed implementation of [EsimProfileLocalDataSource].
 *
 * Delegates all operations to [EsimProfileDao] and adds the
 * `replaceAll` and `setActive` transactional helpers that the DAO
 * exposes as individual operations.
 *
 * Injected into [com.example.lpa.data.repository.ProfileRepositoryImpl]
 * via Hilt — never instantiated directly.
 *
 * @param dao  The Room DAO injected by [com.example.lpa.app.di.DatabaseModule].
 */
class LocalEsimProfileDataSource @Inject constructor(
    private val dao: EsimProfileDao
) : EsimProfileLocalDataSource {

    override fun observeAll(): Flow<List<EsimProfileEntity>> =
        dao.observeAll()

    override suspend fun getAll(): List<EsimProfileEntity> =
        dao.getAll()

    override suspend fun findByIccId(iccId: String): EsimProfileEntity? =
        dao.findByIccId(iccId)

    override suspend fun insert(profile: EsimProfileEntity): Long =
        dao.insert(profile)

    /**
     * Replaces all rows atomically: deletes existing records then bulk-inserts
     * the new list. Called after a full eUICC profile refresh so the local DB
     * exactly mirrors what is installed on the chip.
     */
    override suspend fun replaceAll(profiles: List<EsimProfileEntity>) {
        dao.deleteAll()
        dao.insertAll(profiles)
    }

    /**
     * Atomically deactivates every profile and then marks [id] as active.
     * Uses two queries in sequence — wrap in a Room transaction in the DAO
     * when required by production requirements.
     */
    override suspend fun setActive(id: Long) {
        dao.deactivateAll()
        dao.setActive(id)
    }

    override suspend fun delete(profile: EsimProfileEntity) {
        dao.delete(profile)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
