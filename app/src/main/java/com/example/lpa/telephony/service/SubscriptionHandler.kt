package com.example.lpa.telephony.service

import android.content.Context
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.EsimProfile
import com.example.lpa.domain.models.ProfileState
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
     * Returns the total number of eSIM profiles installed on the device,
     * regardless of whether they are currently active.
     *
     * Uses [SubscriptionManager.getAllSubscriptionInfoList] and filters for
     * embedded entries. Requires [android.Manifest.permission.READ_PHONE_STATE].
     *
     * @return Count of installed eSIM profiles, or 0 on [SecurityException].
     */
    suspend fun getEsimProfileCount(): Int {
        return try {
            SubscriptionManager.from(context)
                .allSubscriptionInfoList
                ?.count { it.isEmbedded }
                ?: 0
        } catch (e: SecurityException) {
            // READ_PHONE_STATE not granted at runtime — degrade gracefully.
            0
        }
    }

    /**
     * Returns the first currently active eSIM subscription, or `null` if none
     * is active or the permission is not granted.
     *
     * Uses [SubscriptionManager.getActiveSubscriptionInfoList] and filters for
     * embedded entries. Requires [android.Manifest.permission.READ_PHONE_STATE].
     *
     * @return The first active embedded [SubscriptionInfo], or `null`.
     */
    suspend fun getActiveEsimSubscription(): SubscriptionInfo? {
        return try {
            subscriptionManager.activeSubscriptionInfoList
                ?.filter { it.isEmbedded }
                ?.firstOrNull()
        } catch (e: SecurityException) {
            // READ_PHONE_STATE not granted at runtime — degrade gracefully.
            null
        }
    }

    /**
     * Returns a list of all eSIM profiles installed on the device.
     * Maps [SubscriptionInfo] to domain [EsimProfile].
     *
     * @return List of [EsimProfile], or empty list on [SecurityException].
     */
    fun getAllEsimProfiles(): List<EsimProfile> {
        return try {
            val allSubscriptions = subscriptionManager.allSubscriptionInfoList ?: emptyList()
            val activeSubscriptions = subscriptionManager.activeSubscriptionInfoList ?: emptyList()
            val activeIccIds = activeSubscriptions.map { it.iccId }.toSet()

            allSubscriptions
                .filter { it.isEmbedded }
                .map { info ->
                    EsimProfile(
                        iccId = info.iccId ?: "",
                        nickname = info.displayName?.toString() ?: "Unknown",
                        serviceProvider = info.carrierName?.toString() ?: "Unknown",
                        state = if (activeIccIds.contains(info.iccId)) {
                            ProfileState.ENABLED
                        } else {
                            ProfileState.DISABLED
                        }
                    )
                }
        } catch (e: SecurityException) {
            emptyList()
        }
    }

    /**
     * Gets the active subscription ID for a given ICCID.
     */
    fun getSubscriptionId(iccId: String): Int? {
        return try {
            subscriptionManager.allSubscriptionInfoList
                ?.find { it.iccId == iccId }
                ?.subscriptionId
        } catch (e: SecurityException) {
            null
        }
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
