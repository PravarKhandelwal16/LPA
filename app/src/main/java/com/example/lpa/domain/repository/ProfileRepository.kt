package com.example.lpa.domain.repository

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.EsimProfile
import kotlinx.coroutines.flow.Flow

/**
 * Contract for eSIM profile CRUD operations on the eUICC.
 *
 * Implemented in the **data** layer ([com.example.lpa.data.repository.ProfileRepositoryImpl]).
 *
 * Exposes a [Flow] for the profile list so the UI can react to changes
 * (e.g. after a profile is enabled/disabled) without polling.
 */
interface ProfileRepository {

    /**
     * A live stream of all profiles currently installed on the eUICC.
     *
     * Emits a new list whenever the profile state changes.
     * Backed by a Room [Flow] in the data layer, which stays in sync
     * with the eUICC via background sync.
     */
    fun observeProfiles(): Flow<List<EsimProfile>>

    /**
     * Fetches the current profile list and stores it locally.
     *
     * Call this to trigger a refresh from the eUICC / remote source.
     * [observeProfiles] will emit the updated list automatically.
     *
     * @return [Result.Success] on success, [Result.Error] on failure.
     */
    suspend fun refreshProfiles(): Result<Unit>
}
