package com.example.lpa.ui.logs

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.lpa.domain.models.LpaLogEntry
import com.example.lpa.domain.models.LogLevel
import com.example.lpa.ui.theme.PrismEsimLpaTheme

/**
 * Logs screen — renders the LPA operation audit log with level filtering.
 *
 * @param paddingValues Padding from the root [Scaffold] bottom bar.
 * @param viewModel     Hilt-injected [LogsViewModel].
 */
@Composable
fun LogsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: LogsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is LogsUiState.Loading -> LogsLoadingContent()
            is LogsUiState.Empty   -> LogsEmptyContent()
            is LogsUiState.Success -> LogsSuccessContent(
                state = state,
                onFilterSelected = viewModel::setFilter,
                onFilterCleared = viewModel::clearFilter
            )
            is LogsUiState.Error   -> LogsErrorContent(message = state.message)
        }
    }
}

// ─── Loading ──────────────────────────────────────────────────────────────────

@Composable
private fun LogsLoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

// ─── Empty ────────────────────────────────────────────────────────────────────

@Composable
private fun LogsEmptyContent() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Description,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No logs yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "LPA operation logs will appear here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Success ──────────────────────────────────────────────────────────────────

@Composable
private fun LogsSuccessContent(
    state: LogsUiState.Success,
    onFilterSelected: (LogLevel) -> Unit,
    onFilterCleared: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Filter chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(LogLevel.entries) { level ->
                FilterChip(
                    selected = state.activeFilter == level,
                    onClick = {
                        if (state.activeFilter == level) onFilterCleared()
                        else onFilterSelected(level)
                    },
                    label = { Text(level.name) }
                )
            }
        }

        // Log entries
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.filteredEntries, key = { it.id }) { entry ->
                LogEntryCard(entry = entry)
            }
        }
    }
}

@Composable
private fun LogEntryCard(entry: LpaLogEntry) {
    val levelColor = when (entry.level) {
        LogLevel.INFO    -> MaterialTheme.colorScheme.primary
        LogLevel.WARNING -> MaterialTheme.colorScheme.tertiary
        LogLevel.ERROR   -> MaterialTheme.colorScheme.error
        LogLevel.DEBUG   -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = entry.level.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = levelColor
                )
                Text(
                    text = entry.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.operation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ─── Error ────────────────────────────────────────────────────────────────────

@Composable
private fun LogsErrorContent(message: String) {
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
private fun LogsEmptyPreview() {
    PrismEsimLpaTheme(darkTheme = true) { LogsEmptyContent() }
}
