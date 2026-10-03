package com.contactunifier.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.presentation.components.DuplicateGroupCard

/**
 * Main screen showing list of duplicate contact groups.
 */
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToDetail: (DuplicateGroup) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val duplicateGroups = viewModel.duplicateGroups.collectAsState().value
    val selectedGroups = viewModel.selectedGroups.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadContacts()
    }

    Scaffold(
        floatingActionButton = {
            if (duplicateGroups.isNotEmpty() && selectedGroups.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { /* Navigate to batch merge */ },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Merge") },
                    text = { Text("Merge ${selectedGroups.size} groups") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                MainUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text(
                            "Scanning contacts...",
                            modifier = Modifier.padding(top = 16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                MainUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(duplicateGroups) { group ->
                            DuplicateGroupCard(
                                group = group,
                                isSelected = selectedGroups.contains(group.groupId),
                                onSelect = { viewModel.toggleGroupSelection(group.groupId) },
                                onNavigateToDetail = { onNavigateToDetail(group) }
                            )
                        }
                    }
                }

                MainUiState.NoDuplicatesFound -> {
                    EmptyStateScreen(
                        title = "No duplicates found",
                        description = "Your contacts are all unique!"
                    )
                }

                MainUiState.NoContactsFound -> {
                    EmptyStateScreen(
                        title = "No contacts",
                        description = "No contacts found on your device."
                    )
                }

                is MainUiState.Error -> {
                    ErrorStateScreen(
                        message = uiState.message,
                        onRetry = { viewModel.loadContacts() }
                    )
                }

                MainUiState.Idle -> {}
            }
        }
    }
}

@Composable
private fun EmptyStateScreen(title: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            description,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorStateScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Error",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            message,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
