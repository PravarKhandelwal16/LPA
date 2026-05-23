package com.example.lpa.core.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Singleton helper for runtime permission checks related to phone state.
 *
 * Usage:
 * ```kotlin
 * if (PermissionHelper.isPhoneStateGranted(context)) {
 *     // safe to read phone state
 * }
 * ```
 */
object PermissionHelper {

    /**
     * Returns `true` if [android.Manifest.permission.READ_PHONE_STATE] has been
     * granted to the application, `false` otherwise.
     *
     * @param context Any valid [Context] (Application, Activity, etc.).
     */
    fun isPhoneStateGranted(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
}
