package com.example.lpa.data.repository

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.DeviceStatus
import com.example.lpa.domain.repository.DeviceStatusRepository
import com.example.lpa.telephony.manager.TelephonyRepository
import javax.inject.Inject

/**
 * Production implementation of [DeviceStatusRepository].
 *
 * Delegates to [TelephonyRepository] to retrieve real eUICC and subscription
 * data from the Android framework. The ViewModel and UI require no changes —
 * they continue to interact with the [DeviceStatusRepository] interface.
 */
class DeviceStatusRepositoryImpl @Inject constructor(
    private val telephonyRepository: TelephonyRepository
) : DeviceStatusRepository {

    override suspend fun getDeviceStatus(): Result<DeviceStatus> =
        telephonyRepository.getDeviceTelephonyStatus()
}

