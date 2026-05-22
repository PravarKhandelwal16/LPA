package com.example.lpa.domain.repository

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.AppSettings
import kotlinx.coroutines.flow.Flow

/**
 * Contract for reading and persisting user app settings / preferences.
 *
 * Implemented in the **data** layer ([com.example.lpa.data.repository.SettingsRepositoryImpl]).
 * Backed by encrypted [androidx.datastore.preferences.core.Preferences] in the data layer.
 */
interface SettingsRepository {

    /**
     * A live stream of the current [AppSettings].
     *
     * Emits immediately with the stored value, then again on every change.
     * Ideal for collecting in a ViewModel so the Settings screen always
     * reflects the persisted state.
     */
    fun observeSettings(): Flow<AppSettings>

    /**
     * Persists an updated [AppSettings] object.
     *
     * @return [Result.Success] on success, [Result.Error] if storage fails.
     */
    suspend fun saveSettings(settings: AppSettings): Result<Unit>
}
