package com.example.lpa.domain.models

/**
 * Domain model representing the overall eSIM device status
 * shown on the Home dashboard.
 *
 * This is a pure Kotlin data class — no Android or framework dependencies.
 * It is produced by the domain layer and consumed by the UI layer.
 *
 * @param isEsimSupported   Whether the device hardware supports eSIM.
 * @param isEuiccAvailable  Whether the eUICC chip is accessible via [EuiccManager].
 * @param activeProfileName Human-readable name of the currently active profile, or null.
 * @param totalProfiles     Total number of profiles installed on the eUICC.
 */
data class DeviceStatus(
    val isEsimSupported: Boolean = false,
    val isEuiccAvailable: Boolean = false,
    val activeProfileName: String? = null,
    val totalProfiles: Int = 0
)
