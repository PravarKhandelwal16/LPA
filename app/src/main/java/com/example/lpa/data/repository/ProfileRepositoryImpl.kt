package com.example.lpa.data.repository

import com.example.lpa.core.result.Result
import com.example.lpa.data.datasource.EsimProfileLocalDataSource
import com.example.lpa.data.mapper.toDomainList
import com.example.lpa.data.mapper.toEntity
import com.example.lpa.domain.models.EsimProfile
import com.example.lpa.domain.models.LogLevel
import com.example.lpa.domain.repository.LogRepository
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
    private val telephonyRepository: TelephonyRepository,
    private val logRepository: LogRepository
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

    override suspend fun switchProfile(iccId: String): Result<Unit> {
        logRepository.addLog("Switching to profile: $iccId", LogLevel.INFO)
        return when (val result = telephonyRepository.switchProfile(iccId)) {
            is Result.Success -> {
                logRepository.addLog("Successfully switched to profile: $iccId", LogLevel.INFO)
                // Optimistically update local state if ICCID is known
                localDataSource.findByIccId(iccId)?.let { entity ->
                    localDataSource.setActive(entity.id)
                }
                // Refresh local state from system for final consistency
                refreshProfiles()
            }
            is Result.Error -> {
                logRepository.addLog("Failed to switch to profile: $iccId. Error: ${result.exception.message}", LogLevel.ERROR)
                Result.Error(result.exception)
            }
            Result.Loading -> Result.Loading
        }
    }

    override suspend fun toggleProfile(iccId: String, enable: Boolean): Result<Unit> {
        // For eSIM, enabling/disabling is typically done via switchToSubscription.
        // If enable is true, we switch to it. If false, we switch to INVALID_SUBSCRIPTION_ID.
        return if (enable) {
            switchProfile(iccId)
        } else {
            // Passing an empty string to switchProfile can be interpreted as disabling 
            // by the underlying EuiccManager (SubscriptionManager.INVALID_SUBSCRIPTION_ID)
            switchProfile("")
        }
    }
}
