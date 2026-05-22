package com.example.lpa.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SimCard
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SimCard
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines all named navigation routes for the PrismEsimLpa app.
 *
 * Each top-level destination carries its own routing metadata (route, label,
 * selected/unselected icons) so the bottom nav bar and nav graph share a
 * single source of truth — no duplicated lists.
 *
 * Pattern for adding a new top-level destination:
 * ```
 * data object NewScreen : Screen(
 *     route         = "new_screen",
 *     label         = "New",
 *     selectedIcon  = Icons.Rounded.SomeIcon,
 *     unselectedIcon = Icons.Outlined.SomeIcon
 * )
 * ```
 *
 * Pattern for adding a detail/nested destination (no bottom-nav entry):
 * ```
 * data object ProfileDetail : Screen(route = "profile_detail/{profileId}")
 * ```
 */
sealed class Screen(
    val route: String,
    val label: String = "",
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
) {
    // ── Top-Level (bottom-nav) destinations ──────────────────────────────────

    /** Main dashboard — device eSIM status at a glance. */
    data object Home : Screen(
        route          = "home",
        label          = "Home",
        selectedIcon   = Icons.Rounded.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard
    )

    /** Installed eSIM profiles list — enable / disable / delete. */
    data object Profiles : Screen(
        route          = "profiles",
        label          = "Profiles",
        selectedIcon   = Icons.Rounded.SimCard,
        unselectedIcon = Icons.Outlined.SimCard
    )

    /** LPA operation audit log — chronological provisioning events. */
    data object Logs : Screen(
        route          = "logs",
        label          = "Logs",
        selectedIcon   = Icons.Rounded.Description,
        unselectedIcon = Icons.Outlined.Description
    )

    /** App preferences — SM-DP+ config, security, theme. */
    data object Settings : Screen(
        route          = "settings",
        label          = "Settings",
        selectedIcon   = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    companion object {
        /**
         * Ordered list of all top-level destinations that appear
         * in the [PrismBottomNavBar]. The order here controls tab order.
         */
        val topLevelDestinations: List<Screen> = listOf(
            Home, Profiles, Logs, Settings
        )

        /**
         * Returns `true` if the given [route] belongs to a top-level
         * destination (i.e., it should show the bottom bar).
         */
        fun isTopLevel(route: String?): Boolean =
            topLevelDestinations.any { it.route == route }
    }
}
