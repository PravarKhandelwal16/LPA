package com.example.lpa.domain.usecase

import com.example.lpa.core.result.Result
import com.example.lpa.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to trigger a refresh of eSIM profiles from the eUICC.
 */
class RefreshProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    /**
     * Executes the profile refresh operation.
     *
     * @return [Result.Success] on success, [Result.Error] on failure.
     */
    suspend operator fun invoke(): Result<Unit> {
        return profileRepository.refreshProfiles()
    }
}
