package com.example.lpa.domain.usecase

import com.example.lpa.domain.models.EsimProfile
import com.example.lpa.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe the stream of installed eSIM profiles.
 *
 * Exposes a [Flow] that re-emits automatically whenever the underlying
 * data changes (e.g. after a refresh from the eUICC, or a profile is enabled).
 */
class ObserveProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    /**
     * @return A live stream of [EsimProfile] objects.
     */
    operator fun invoke(): Flow<List<EsimProfile>> {
        return profileRepository.observeProfiles()
    }
}
