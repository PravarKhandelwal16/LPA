package com.example.lpa.domain.models

/**
 * Domain model representing a single eSIM profile installed on the eUICC chip.
 *
 * Fields map conceptually to the GSMA RSP spec (SGP.22) profile metadata,
 * but contain no platform types — safe for use in pure unit tests.
 *
 * @param iccId          The Integrated Circuit Card Identifier — unique per profile.
 * @param nickname       User-assigned or operator-assigned display name.
 * @param serviceProvider The Mobile Network Operator (MNO) or MVNO name.
 * @param state          Current activation state of the profile on the eUICC.
 */
data class EsimProfile(
    val iccId: String,
    val nickname: String,
    val serviceProvider: String,
    val state: ProfileState
)

/**
 * Possible activation states of an eSIM profile as defined by GSMA SGP.22.
 */
enum class ProfileState {
    /** Profile is currently active and in use. */
    ENABLED,

    /** Profile is installed but not active. */
    DISABLED,

    /** Profile has been deleted from the eUICC. */
    DELETED
}
