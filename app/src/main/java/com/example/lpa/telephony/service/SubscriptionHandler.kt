package com.example.lpa.telephony.service

import android.content.Context
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.example.lpa.core.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles operations related to [SubscriptionManager].
 *
 * While [com.example.lpa.telephony.manager.EuiccManagerWrapper] handles
 * the eUICC chip itself (downloading, deleting profiles), the
 * SubscriptionManager is required for observing active network states,
 * carrier names, and mapping ICCIDs to Subscription IDs.
 *
 * @param context Application context used to obtain the [SubscriptionManager] system service.
 */
@Singleton
class SubscriptionHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Lazily obtained [SubscriptionManager]. */
    private val subscriptionManager: SubscriptionManager by lazy {
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    }

    /**
     * Returns a list of active [SubscriptionInfo] records that are backed by
     * an embedded (eSIM) profile. Requires [android.Manifest.permission.READ_PHONE_STATE].
     *
     * @return Active embedded subscriptions, or an empty list if the permission
     *         is not yet granted or no embedded subscriptions are active.
     */
    fun getActiveEmbeddedSubscriptions(): List<SubscriptionInfo> {
        return try {
            subscriptionManager.activeSubscriptionInfoList
                ?.filter { it.isEmbedded }
                ?: emptyList()
        } catch (e: SecurityException) {
            // READ_PHONE_STATE not granted at runtime — degrade gracefully.
            emptyList()
        }
    }

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
