package com.example.lpa.domain.models

/**
 * Domain model representing the SM-DP+ server network configuration
 * used for eSIM profile provisioning.
 *
 * Derived from [AppSettings] at the repository layer so the network
 * layer receives a typed config rather than raw string fields.
 *
 * @param serverUrl          Full SM-DP+ server base URL (e.g. "https://smdp.example.com").
 * @param isCertPinningEnabled Whether strict TLS certificate pinning is enforced.
 * @param timeoutSeconds     HTTP connection/read timeout in seconds.
 */
data class NetworkConfig(
    val serverUrl: String,
    val isCertPinningEnabled: Boolean = true,
    val timeoutSeconds: Long = 30L
)
