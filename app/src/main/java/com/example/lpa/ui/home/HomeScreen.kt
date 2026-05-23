package com.example.lpa.ui.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lpa.core.permission.PermissionHelper
import com.example.lpa.ui.theme.PrismEsimLpaTheme

/**
 * Home screen — renders based on the current [HomeUiState] from [HomeViewModel].
 *
 * Collects state with [collectAsStateWithLifecycle] so collection is paused
 * when the app goes to the background, preventing unnecessary work.
 *
 * @param paddingValues Padding from the root [Scaffold] bottom bar.
 * @param viewModel     Hilt-injected [HomeViewModel] (auto-provided in production).
 */
@Composable
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Launcher: refresh immediately if the permission was granted, otherwise no-op.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.refresh()
    }

    // On first composition: if the permission is already held refresh straight away,
    // otherwise prompt the user for READ_PHONE_STATE.
    LaunchedEffect(Unit) {
        if (PermissionHelper.isPhoneStateGranted(context)) {
            viewModel.refresh()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> HomeLoadingContent()
            is HomeUiState.Success -> HomeSuccessContent(state = state)
            is HomeUiState.Error   -> HomeErrorContent(
                message = state.message,
                onRetry = viewModel::refresh
            )
        }
    }
}

// ─── Loading ──────────────────────────────────────────────────────────────────

@Composable
private fun HomeLoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

// ─── Success ──────────────────────────────────────────────────────────────────

@Composable
private fun HomeSuccessContent(state: HomeUiState.Success) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Dashboard,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "eSIM supported: ${state.status.isEsimSupported}\n" +
                "eUICC available: ${state.status.isEuiccAvailable}\n" +
                "Profiles: ${state.status.totalProfiles}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Error ────────────────────────────────────────────────────────────────────

@Composable
private fun HomeErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        if (message.contains("permission", ignoreCase = true)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Phone state access is required to read eSIM and subscription information. " +
                    "Please grant the permission and retry.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        FilledTonalButton(onClick = onRetry) {
            Icon(Icons.Rounded.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Retry")
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PrismEsimLpaTheme(darkTheme = true) {
        HomeSuccessContent(
            state = HomeUiState.Success(
                com.example.lpa.domain.models.DeviceStatus(
                    isEsimSupported = true,
                    isEuiccAvailable = true,
                    totalProfiles = 2
                )
            )
        )
    }
}
