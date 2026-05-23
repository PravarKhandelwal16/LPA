package com.example.lpa.domain.usecase

import com.example.lpa.core.result.Result
import com.example.lpa.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to toggle the state of a specific eSIM profile (enable/disable).
 */
class ToggleProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    /**
     * Executes the profile toggle operation.
     *
     * @param iccId  The ICCID of the profile to toggle.
     * @param enable True to enable, false to disable.
     * @return [Result.Success] on success, [Result.Error] on failure.
     */
    suspend operator fun invoke(iccId: String, enable: Boolean): Result<Unit> {
        return profileRepository.toggleProfile(iccId, enable)
    }
}
