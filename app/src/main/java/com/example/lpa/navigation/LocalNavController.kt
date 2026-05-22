package com.example.lpa.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

/**
 * A [staticCompositionLocalOf] that provides the app's [NavHostController]
 * anywhere in the Compose tree without prop-drilling.
 *
 * Usage from any composable deep in the hierarchy:
 * ```kotlin
 * val navController = LocalNavController.current
 * navController.navigate(Screen.Profiles.route)
 * ```
 *
 * The controller is provided by [AppNavGraph] at the root of the composition.
 * Do **not** override this value at arbitrary points in the tree.
 */
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error(
        "No NavHostController found. " +
            "Did you forget to wrap your content with AppNavGraph?"
    )
}
