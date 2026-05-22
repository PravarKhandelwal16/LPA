package com.example.lpa.data.repository

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.AppSettings
import com.example.lpa.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stub implementation of [SettingsRepository].
 *
 * Uses an in-memory [MutableStateFlow] until the DataStore layer is connected.
 * Replace the in-memory flow with a real DataStore [Flow] when ready.
 *
 * Annotated with [@Singleton][Singleton] so the same in-memory state is shared
 * across all injection sites during a single process lifetime.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    // TODO: inject DataStore<Preferences> for persistent encrypted storage
) : SettingsRepository {

    private val _settings = MutableStateFlow(AppSettings())

    override fun observeSettings(): Flow<AppSettings> {
        // Stub — real implementation: dataStore.data.map { prefs -> prefs.toAppSettings() }
        return _settings.asStateFlow()
    }

    override suspend fun saveSettings(settings: AppSettings): Result<Unit> {
        // Stub — real implementation: dataStore.edit { prefs -> prefs.fromAppSettings(settings) }
        _settings.value = settings
        return Result.Success(Unit)
    }
}
