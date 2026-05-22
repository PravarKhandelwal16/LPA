package com.example.lpa.data.mapper

import com.example.lpa.domain.models.DeviceStatus

/**
 * Mapper for [DeviceStatus] domain model.
 *
 * Currently the data source for [DeviceStatus] is the Android telephony system
 * (not a database entity or network DTO), so the mapper converts from a raw
 * platform data holder rather than from a Room entity.
 *
 * ## Usage
 * When `EuiccManagerWrapper` is implemented, it will return a `EuiccInfo`
 * wrapper. Use [fromPlatform] to convert it cleanly at the repository boundary:
 * ```kotlin
 * val info = euiccManagerWrapper.getEuiccInfo()
 * val status = DeviceStatusMapper.fromPlatform(
 *     isSupported       = euiccManager.isEnabled,
 *     isAvailable       = info != null,
 *     activeProfileName = profileManager.activeProfile?.nickname,
 *     totalProfiles     = profileManager.installedProfiles.size
 * )
 * ```
 */
object DeviceStatusMapper {

    /**
     * Constructs a [DeviceStatus] domain model from raw platform values.
     *
     * All parameters have safe defaults so this can be called with partial
     * data during initial load before the telephony layer is ready.
     *
     * @param isSupported       `EuiccManager.isEnabled` result.
     * @param isAvailable       Whether `EuiccManager.euiccInfo` is non-null.
     * @param activeProfileName Display name of the currently active profile, or null.
     * @param totalProfiles     Count of installed profiles from the local DB or eUICC.
     */
    fun fromPlatform(
        isSupported: Boolean = false,
        isAvailable: Boolean = false,
        activeProfileName: String? = null,
        totalProfiles: Int = 0
    ): DeviceStatus = DeviceStatus(
        isEsimSupported   = isSupported,
        isEuiccAvailable  = isAvailable,
        activeProfileName = activeProfileName,
        totalProfiles     = totalProfiles
    )

    /**
     * Returns a safe default [DeviceStatus] representing an unavailable or
     * unsupported eSIM state. Used as a fallback on errors or before first load.
     */
    fun unavailable(): DeviceStatus = fromPlatform()
}
