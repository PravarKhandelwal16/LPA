package com.example.lpa.data.repository

import com.example.lpa.core.result.Result
import com.example.lpa.data.datasource.EsimProfileLocalDataSource
import com.example.lpa.data.mapper.toDomainList
import com.example.lpa.data.mapper.toEntity
import com.example.lpa.domain.models.EsimProfile
import com.example.lpa.domain.repository.ProfileRepository
import com.example.lpa.telephony.manager.TelephonyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Production implementation of [ProfileRepository] backed by Room.
 *
 * Bridges the [EsimProfileLocalDataSource] (data layer) to the domain layer using the
 * mapper extensions in `com.example.lpa.data.mapper` (like `toDomain()`).
 *
 * The [observeProfiles] [Flow] is Room-backed — it automatically re-emits
 * whenever the `esim_profiles` table changes, so the UI stays in sync without
 * polling.
 *
 * [refreshProfiles] is a no-op stub at this stage. It will be replaced with
 * a real eUICC query (via `EuiccManagerWrapper`) that upserts results into
 * the Room database — triggering [observeProfiles] to re-emit automatically.
 *
 * @param localDataSource  Hilt-injected local data source for profile persistence.
 */
class ProfileRepositoryImpl @Inject constructor(
    private val localDataSource: EsimProfileLocalDataSource,
    private val telephonyRepository: TelephonyRepository
) : ProfileRepository {

    /**
     * Returns a [Flow] of domain [EsimProfile] objects backed by the Room DAO.
     *
     * Emits immediately with the current DB contents, then re-emits whenever
     * the `esim_profiles` table is modified. The [toDomainList] mapper is
     * applied in the Flow chain — the ViewModel receives clean domain models.
     */
    override fun observeProfiles(): Flow<List<EsimProfile>> =
        localDataSource.observeAll().map { entities -> entities.toDomainList() }

    /**
     * Triggers a refresh of profile data from the eUICC.
     */
    override suspend fun refreshProfiles(): Result<Unit> {
        return when (val result = telephonyRepository.getInstalledProfiles()) {
            is Result.Success -> {
                val entities = result.data.map { it.toEntity() }
                localDataSource.replaceAll(entities)
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.exception)
            Result.Loading -> Result.Loading
        }
    }
}
