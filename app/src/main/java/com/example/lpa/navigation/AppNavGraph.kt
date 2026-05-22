package com.example.lpa.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lpa.ui.home.HomeScreen
import com.example.lpa.ui.logs.LogsScreen
import com.example.lpa.ui.profiles.ProfilesScreen
import com.example.lpa.ui.settings.SettingsScreen

// ─── Transition Constants ─────────────────────────────────────────────────────

private const val TRANSITION_DURATION_MS = 220
private const val SCALE_INITIAL = 0.95f

// ─── Navigation Helper ────────────────────────────────────────────────────────

/**
 * Navigates to a top-level [Screen] using the recommended back-stack strategy:
 * - Pops up to the start destination to avoid a growing back-stack.
 * - Saves & restores each destination's state on tab switches.
 * - Ensures only one copy of each destination exists at a time.
 */
private fun NavHostController.navigateToTopLevel(screen: Screen) {
    navigate(screen.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

// ─── Root Nav Graph ───────────────────────────────────────────────────────────

/**
 * Root navigation host for PrismEsimLpa.
 *
 * Responsibilities:
 * - Provides the [NavHostController] via [LocalNavController] to the entire
 *   Compose tree (eliminates prop-drilling for nested navigation actions).
 * - Renders a [Scaffold] with the [PrismBottomNavBar] that auto-hides on
 *   non-top-level destinations.
 * - Applies a consistent fade + subtle scale transition between all destinations.
 * - Declares all [composable] destinations in one place — the canonical registry.
 *
 * Adding a new top-level screen:
 * 1. Add a `data object` to [Screen] with route, label, and icons.
 * 2. Add a `composable(Screen.NewScreen.route)` block below.
 *
 * Adding a detail/modal screen (no bottom tab):
 * 1. Add a route-only `data object` to [Screen].
 * 2. Add a `composable(Screen.Detail.route)` block below.
 * 3. The bottom bar hides automatically via [Screen.isTopLevel].
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Provide the controller app-wide via CompositionLocal
    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                PrismBottomNavBar(
                    currentRoute = currentRoute,
                    onDestinationSelected = { screen ->
                        navController.navigateToTopLevel(screen)
                    },
                    visible = Screen.isTopLevel(currentRoute)
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    // ── Shared axis: fade + scale in ──────────────────────
                    enterTransition = {
                        fadeIn(tween(TRANSITION_DURATION_MS)) +
                            scaleIn(
                                tween(TRANSITION_DURATION_MS),
                                initialScale = SCALE_INITIAL
                            )
                    },
                    exitTransition = {
                        fadeOut(tween(TRANSITION_DURATION_MS)) +
                            scaleOut(
                                tween(TRANSITION_DURATION_MS),
                                targetScale = SCALE_INITIAL
                            )
                    },
                    popEnterTransition = {
                        fadeIn(tween(TRANSITION_DURATION_MS)) +
                            scaleIn(
                                tween(TRANSITION_DURATION_MS),
                                initialScale = SCALE_INITIAL
                            )
                    },
                    popExitTransition = {
                        fadeOut(tween(TRANSITION_DURATION_MS)) +
                            scaleOut(
                                tween(TRANSITION_DURATION_MS),
                                targetScale = SCALE_INITIAL
                            )
                    }
                ) {
                    // ── Top-level destinations ────────────────────────────
                    composable(Screen.Home.route) {
                        HomeScreen(paddingValues = innerPadding)
                    }
                    composable(Screen.Profiles.route) {
                        ProfilesScreen(paddingValues = innerPadding)
                    }
                    composable(Screen.Logs.route) {
                        LogsScreen(paddingValues = innerPadding)
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(paddingValues = innerPadding)
                    }

                    // ── Detail / nested destinations (add below) ──────────
                    // composable(Screen.ProfileDetail.route) { backStackEntry ->
                    //     val profileId = backStackEntry.arguments?.getString("profileId")
                    //     ProfileDetailScreen(profileId = profileId, paddingValues = innerPadding)
                    // }
                }
            }
        }
    }
}
