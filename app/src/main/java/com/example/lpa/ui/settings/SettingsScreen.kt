package com.example.lpa.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lpa.domain.models.AppSettings
import com.example.lpa.ui.theme.PrismEsimLpaTheme

/**
 * Settings screen — renders app preference controls.
 *
 * Each control calls a specific ViewModel action on change so settings
 * are persisted immediately with optimistic UI updates.
 *
 * @param paddingValues Padding from the root [Scaffold] bottom bar.
 * @param viewModel     Hilt-injected [SettingsViewModel].
 */
@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is SettingsUiState.Loading -> SettingsLoadingContent()
            is SettingsUiState.Success -> SettingsSuccessContent(
                state = state,
                onSmdpUrlChange = viewModel::onSmdpServerUrlChange,
                onDarkThemeToggle = viewModel::onDarkThemeToggle,
                onCertPinningToggle = viewModel::onCertificatePinningToggle
            )
            is SettingsUiState.Error -> SettingsErrorContent(message = state.message)
        }
    }
}

// ─── Loading ──────────────────────────────────────────────────────────────────

@Composable
private fun SettingsLoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

// ─── Success ──────────────────────────────────────────────────────────────────

@Composable
private fun SettingsSuccessContent(
    state: SettingsUiState.Success,
    onSmdpUrlChange: (String) -> Unit,
    onDarkThemeToggle: (Boolean) -> Unit,
    onCertPinningToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ── Network ───────────────────────────────────────────────────────
        SectionHeader(title = "Network")
        OutlinedTextField(
            value = state.settings.smdpServerUrl,
            onValueChange = onSmdpUrlChange,
            label = { Text("SM-DP+ Server URL") },
            placeholder = { Text("https://smdp.example.com") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // ── Security ──────────────────────────────────────────────────────
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        SectionHeader(title = "Security")
        SettingsToggleRow(
            label = "Certificate Pinning",
            description = "Enforce strict TLS certificate validation",
            checked = state.settings.isCertificatePinningEnabled,
            onCheckedChange = onCertPinningToggle
        )

        // ── Appearance ────────────────────────────────────────────────────
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        SectionHeader(title = "Appearance")
        SettingsToggleRow(
            label = "Dark Theme",
            description = "Use dark color scheme",
            checked = state.settings.isDarkThemeEnabled,
            onCheckedChange = onDarkThemeToggle
        )

        // ── Logs ──────────────────────────────────────────────────────────
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        SectionHeader(title = "Logs")
        Text(
            text = "Retention period: ${state.settings.logRetentionDays} days",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun SettingsToggleRow(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

// ─── Error ────────────────────────────────────────────────────────────────────

@Composable
private fun SettingsErrorContent(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
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
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun SettingsSuccessPreview() {
    PrismEsimLpaTheme(darkTheme = true) {
        SettingsSuccessContent(
            state = SettingsUiState.Success(settings = AppSettings()),
            onSmdpUrlChange = {},
            onDarkThemeToggle = {},
            onCertPinningToggle = {}
        )
    }
}
