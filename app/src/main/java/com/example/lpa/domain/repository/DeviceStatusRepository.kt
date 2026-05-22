package com.example.lpa.domain.repository

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.DeviceStatus

/**
 * Contract for accessing device-level eSIM status information.
 *
 * Implemented in the **data** layer ([com.example.lpa.data.repository.DeviceStatusRepositoryImpl]).
 * The domain and UI layers depend only on this interface — never on the implementation.
 *
 * All functions are `suspend` to be called from coroutines in ViewModels.
 */
interface DeviceStatusRepository {

    /**
     * Returns the current eSIM/eUICC status of the device.
     *
     * @return [Result.Success] wrapping [DeviceStatus], or [Result.Error] on failure.
     */
    suspend fun getDeviceStatus(): Result<DeviceStatus>
}
