package com.example.lpa.domain.models

/**
 * Domain model representing the user-facing app settings / preferences.
 *
 * @param smdpServerUrl         The SM-DP+ server URL for profile provisioning.
 * @param isDarkThemeEnabled    Whether the user has opted in to dark theme.
 * @param isCertificatePinningEnabled Whether strict TLS cert pinning is active.
 * @param logRetentionDays      Number of days to retain LPA operation logs.
 */
data class AppSettings(
    val smdpServerUrl: String = "",
    val isDarkThemeEnabled: Boolean = true,
    val isCertificatePinningEnabled: Boolean = true,
    val logRetentionDays: Int = 30
)
