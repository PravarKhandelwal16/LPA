package com.example.lpa.data.mapper

import com.example.lpa.domain.models.AppSettings
import com.example.lpa.domain.models.NetworkConfig

// ─── AppSettings → NetworkConfig ──────────────────────────────────────────────

/**
 * Derives a [NetworkConfig] from the current [AppSettings].
 *
 * The network layer depends only on [NetworkConfig] — it never imports
 * [AppSettings] directly. This mapper is the sole bridge between the two.
 *
 * @receiver The current app preferences.
 * @return A typed network configuration ready for injection into Retrofit/OkHttp.
 */
fun AppSettings.toNetworkConfig(): NetworkConfig = NetworkConfig(
    serverUrl             = smdpServerUrl,
    isCertPinningEnabled  = isCertificatePinningEnabled,
    timeoutSeconds        = 30L
)

// ─── Map → AppSettings (e.g. from DataStore Preferences) ──────────────────────

/**
 * Reconstructs an [AppSettings] from a raw key→value preferences map.
 *
 * Use this when reading from [androidx.datastore.preferences.core.Preferences]
 * in `SettingsRepositoryImpl`:
 * ```kotlin
 * dataStore.data.map { prefs ->
 *     prefs.toAppSettings()
 * }
 * ```
 */
fun Map<String, Any?>.toAppSettings(): AppSettings = AppSettings(
    smdpServerUrl                = this[AppSettingsKeys.SMDP_SERVER_URL] as? String ?: "",
    isDarkThemeEnabled           = this[AppSettingsKeys.DARK_THEME] as? Boolean ?: true,
    isCertificatePinningEnabled  = this[AppSettingsKeys.CERT_PINNING] as? Boolean ?: true,
    logRetentionDays             = this[AppSettingsKeys.LOG_RETENTION_DAYS] as? Int ?: 30
)

/**
 * DataStore preference key constants for [AppSettings] persistence.
 *
 * Centralised here so both `SettingsRepositoryImpl` and the mapper share
 * the same key strings without risk of typos.
 */
object AppSettingsKeys {
    const val SMDP_SERVER_URL       = "smdp_server_url"
    const val DARK_THEME            = "dark_theme_enabled"
    const val CERT_PINNING          = "cert_pinning_enabled"
    const val LOG_RETENTION_DAYS    = "log_retention_days"
}
