package com.example.lpa.telephony.service

import com.example.lpa.core.result.Result
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles operations related to [android.telephony.SubscriptionManager].
 *
 * While [com.example.lpa.telephony.manager.EuiccManagerWrapper] handles
 * the eUICC chip itself (downloading, deleting profiles), the
 * SubscriptionManager is required for observing active network states,
 * carrier names, and mapping ICCIDs to Subscription IDs.
 *
 * **Note:** Placeholder architecture for future implementation.
 */
@Singleton
class SubscriptionHandler @Inject constructor() {

    /**
     * Gets the active subscription ID for a given ICCID.
     */
    suspend fun getSubscriptionId(iccId: String): Int? {
        // Placeholder: Will map ICCID to Subscription ID using SubscriptionManager
        return null
    }

    /**
     * Checks if a profile is actively registered on a network.
     */
    suspend fun isProfileActive(subscriptionId: Int): Boolean {
        // Placeholder: Will check SubscriptionManager.getActiveSubscriptionInfo
        return false
    }

    /**
     * Updates the local nickname of a subscription in the system settings.
     */
    suspend fun updateSubscriptionNickname(subscriptionId: Int, nickname: String): Result<Unit> {
        // Placeholder: Will call SubscriptionManager.setDisplayName
        return Result.Success(Unit)
    }
}
