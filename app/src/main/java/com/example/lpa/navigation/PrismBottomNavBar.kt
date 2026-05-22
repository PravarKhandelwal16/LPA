package com.example.lpa.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Material 3 bottom navigation bar for PrismEsimLpa.
 *
 * Renders one [NavigationBarItem] per entry in [Screen.topLevelDestinations].
 * Uses the filled icon variant when selected and the outlined variant when not,
 * following Material 3 icon usage guidelines.
 *
 * Animates in/out vertically so it can be hidden on detail screens by
 * toggling [visible] from outside (driven by [AppNavGraph]).
 *
 * @param currentRoute   The route string of the currently active destination.
 * @param onDestinationSelected Callback invoked with the tapped [Screen].
 * @param visible        Whether the bar is visible (animates when toggled).
 * @param modifier       Optional [Modifier].
 */
@Composable
fun PrismBottomNavBar(
    currentRoute: String?,
    onDestinationSelected: (Screen) -> Unit,
    visible: Boolean = true,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + slideInVertically(tween(200)) { it },
        exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it },
        modifier = modifier
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Screen.topLevelDestinations.forEach { screen ->
                val selected = currentRoute == screen.route

                NavigationBarItem(
                    selected = selected,
                    onClick = { onDestinationSelected(screen) },
                    icon = {
                        val icon = if (selected) screen.selectedIcon else screen.unselectedIcon
                        if (icon != null) {
                            Icon(
                                imageVector = icon,
                                contentDescription = screen.label
                            )
                        }
                    },
                    label = {
                        Text(
                            text = screen.label,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = MaterialTheme.colorScheme.primary,
                        selectedTextColor   = MaterialTheme.colorScheme.primary,
                        indicatorColor      = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
