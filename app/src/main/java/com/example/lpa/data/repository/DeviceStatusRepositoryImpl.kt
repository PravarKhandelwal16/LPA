package com.example.lpa.data.repository

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.DeviceStatus
import com.example.lpa.domain.repository.DeviceStatusRepository
import javax.inject.Inject

/**
 * Stub implementation of [DeviceStatusRepository].
 *
 * Returns a static [DeviceStatus] until the telephony layer
 * ([com.example.lpa.telephony.euicc.EuiccManagerWrapper]) is wired in.
 *
 * Replace the body of [getDeviceStatus] with a real call to the
 * telephony wrapper when ready — the ViewModel and UI require no changes.
 */
class DeviceStatusRepositoryImpl @Inject constructor(
    // TODO: inject EuiccManagerWrapper when telephony layer is implemented
) : DeviceStatusRepository {

    override suspend fun getDeviceStatus(): Result<DeviceStatus> {
        // Stub — real implementation will query EuiccManager
        return Result.Success(
            DeviceStatus(
                isEsimSupported = false,
                isEuiccAvailable = false,
                activeProfileName = null,
                totalProfiles = 0
            )
        )
    }
}
