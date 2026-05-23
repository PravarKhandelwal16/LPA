package com.example.lpa.telephony.manager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SubscriptionManager
import android.telephony.euicc.EuiccManager
import com.example.lpa.core.result.Result
import com.example.lpa.domain.models.EsimProfile
import com.example.lpa.telephony.service.SubscriptionHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for the Android framework [EuiccManager].
 *
 * Provides a coroutine-safe, mockable boundary between the app's business
 * logic and the system-level eSIM APIs. All actual `EuiccManager` method calls
 * should be contained within this class.
 *
 * @param context Application context used to obtain the [EuiccManager] system service.
 */
@Singleton
class EuiccManagerWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val subscriptionHandler: SubscriptionHandler
) {

    /** Lazily obtained [EuiccManager]. Null on devices that lack eSIM hardware. */
    private val euiccManager: EuiccManager? by lazy {
        context.getSystemService(Context.EUICC_SERVICE) as? EuiccManager
    }

    /**
     * Returns true if the device has eSIM hardware by querying [android.content.pm.PackageManager].
     * This does NOT guarantee the chip is enabled or ready.
     */
    suspend fun isEsimSupported(): Boolean =
        context.packageManager.hasSystemFeature("android.hardware.telephony.euicc")

    /**
     * Returns true if the eUICC chip is present **and** currently enabled.
     * Safe to call from any thread.
     */
    suspend fun isEuiccAvailable(): Boolean = euiccManager?.isEnabled ?: false

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
    suspend fun switchProfile(iccId: String): Result<Unit> {
        val manager = euiccManager ?: return Result.Error(IllegalStateException("EuiccManager not available"))
        
        val subId = if (iccId.isEmpty()) {
            SubscriptionManager.INVALID_SUBSCRIPTION_ID
        } else {
            subscriptionHandler.getSubscriptionId(iccId) 
                ?: SubscriptionManager.INVALID_SUBSCRIPTION_ID
        }

        return try {
            // Create a PendingIntent for the callback. 
            // In a production app, we'd register a BroadcastReceiver to listen for this.
            val intent = Intent("com.example.lpa.ACTION_SWITCH_RESULT")
            val callbackIntent = PendingIntent.getBroadcast(
                context, 
                0, 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            manager.switchToSubscription(subId, callbackIntent)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Enables or disables a specific profile by its ICCID or Subscription ID.
     * @deprecated Use [switchProfile]
     */
    suspend fun toggleProfile(iccId: String, enable: Boolean): Result<Unit> {
        return switchProfile(iccId)
    }

    /**
     * Deletes a profile from the eUICC completely.
     */
    suspend fun deleteProfile(iccId: String): Result<Unit> {
        // Placeholder: will call euiccManager.deleteSubscription
        return Result.Success(Unit)
    }
}
