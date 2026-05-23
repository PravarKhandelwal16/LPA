package com.example.lpa.domain.usecase

import com.example.lpa.core.result.Result
import com.example.lpa.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to switch the active eSIM profile.
 */
class SwitchProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    /**
     * Executes the profile switch operation.
     *
     * @param iccId The ICCID of the profile to activate.
     * @return [Result.Success] on success, [Result.Error] on failure.
     */
    suspend operator fun invoke(iccId: String): Result<Unit> {
        return profileRepository.switchProfile(iccId)
    }
}
