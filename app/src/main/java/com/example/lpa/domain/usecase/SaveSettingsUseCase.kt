package com.example.lpa.domain.usecase

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.AppSettings
import com.example.lpa.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Use case to persist user app settings.
 */
class SaveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    /**
     * Executes the settings save operation.
     *
     * @param settings The updated [AppSettings] object to persist.
     * @return [Result.Success] on success, [Result.Error] on failure.
     */
    suspend operator fun invoke(settings: AppSettings): Result<Unit> {
        return settingsRepository.saveSettings(settings)
    }
}
