package com.contactunifier.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.presentation.components.ContactAvatar
import com.contactunifier.presentation.components.MasterLocationSelector

/**
 * Detail screen for reviewing and merging duplicate contacts.
 */
@Composable
fun DetailScreen(group: DuplicateGroup) {
    var selectedLocation by remember { mutableStateOf<String?>(null) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                "Merge ${group.contacts.size} Contacts",
                style = MaterialTheme.typography.headlineMedium
            )

            // Contact preview
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                group.contacts.forEach { contact ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ContactAvatar(contact.name, size = 56.dp)
                        Text(contact.name, style = MaterialTheme.typography.bodyMedium)
                        contact.phoneNumbers.firstOrNull()?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Master location selector
            MasterLocationSelector(
                selectedLocation = selectedLocation,
                onLocationSelected = { selectedLocation = it }
            )

            // Merge button
            Button(
                onClick = { /* Handle merge */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedLocation != null
            ) {
                Text("Merge & Keep in ${selectedLocation ?: "..."}")
            }
        }
    }
}
