package com.example.lpa.telephony.manager

import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.EsimProfile
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for the Android framework [android.telephony.euicc.EuiccManager].
 *
 * Provides a coroutine-safe, mockable boundary between the app's business
 * logic and the system-level eSIM APIs. All actual `EuiccManager` method calls
 * should be contained within this class.
 *
 * **Note:** This is currently a placeholder architecture. Real eUICC
 * system calls will be implemented here later.
 */
@Singleton
class EuiccManagerWrapper @Inject constructor() {

    /**
     * Checks if the device hardware supports eSIM and if the eUICC chip is available.
     */
    suspend fun isEuiccAvailable(): Boolean {
        // Placeholder: will call euiccManager.isEnabled
        return false
    }

    /**
     * Retrieves the list of profiles currently installed on the eUICC.
     * Requires privileged LPA permissions.
     */
    suspend fun getInstalledProfiles(): Result<List<EsimProfile>> {
        // Placeholder: will call euiccManager.downloadSubscription / getSubscriptionInfoList
        return Result.Success(emptyList())
    }

    /**
     * Downloads and installs a new eSIM profile using the provided activation code.
     */
    suspend fun downloadProfile(activationCode: String): Result<Unit> {
        // Placeholder: will handle the DownloadSubscriptionIntent flow
        return Result.Success(Unit)
    }

    /**
     * Enables or disables a specific profile by its ICCID or Subscription ID.
     */
    suspend fun toggleProfile(iccId: String, enable: Boolean): Result<Unit> {
        // Placeholder: will call euiccManager.switchToSubscription
        return Result.Success(Unit)
    }

    /**
     * Deletes a profile from the eUICC completely.
     */
    suspend fun deleteProfile(iccId: String): Result<Unit> {
        // Placeholder: will call euiccManager.deleteSubscription
        return Result.Success(Unit)
    }
}
