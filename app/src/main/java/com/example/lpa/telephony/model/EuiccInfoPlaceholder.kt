package com.example.lpa.telephony.model

/**
 * Placeholder model for raw eUICC chip information.
 *
 * This represents low-level data returned directly by the system APIs
 * (e.g. `android.telephony.euicc.EuiccInfo`) before it is mapped to
 * the app's domain models by the telephony repository layer.
 */
data class EuiccInfoPlaceholder(
    val osVersion: String?,
    val isRemovable: Boolean = false
)
