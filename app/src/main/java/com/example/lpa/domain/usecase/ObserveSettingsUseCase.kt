package com.example.lpa.domain.usecase

import com.example.lpa.domain.models.AppSettings
import com.example.lpa.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe the stream of user app settings.
 */
class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    /**
     * @return A live stream of [AppSettings] objects.
     */
    operator fun invoke(): Flow<AppSettings> {
        return settingsRepository.observeSettings()
    }
}
