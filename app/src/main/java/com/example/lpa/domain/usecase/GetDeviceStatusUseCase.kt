package com.example.lpa.domain.usecase

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.DeviceStatus
import com.example.lpa.domain.repository.DeviceStatusRepository
import javax.inject.Inject

/**
 * Use case to fetch the current eSIM and eUICC support status of the device.
 */
class GetDeviceStatusUseCase @Inject constructor(
    private val deviceStatusRepository: DeviceStatusRepository
) {
    /**
     * Executes the status fetch operation.
     *
     * @return [Result.Success] containing the [DeviceStatus], or [Result.Error] on failure.
     */
    suspend operator fun invoke(): Result<DeviceStatus> {
        return deviceStatusRepository.getDeviceStatus()
    }
}
