package com.contactunifier.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.contactunifier.data.model.Contact
import com.contactunifier.data.model.DuplicateGroup

/**
 * Composable for displaying a duplicate contact group in a card.
 */
@Composable
fun DuplicateGroupCard(
    group: DuplicateGroup,
    isSelected: Boolean = false,
    onSelect: () -> Unit = {},
    onNavigateToDetail: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToDetail() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox for batch selection
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelect() }
            )

            // Contact avatars (show up to 3)
            Row(
                modifier = Modifier.size(48.dp),
                horizontalArrangement = Arrangement.spacedBy((-12).dp)
            ) {
                group.contacts.take(3).forEach { contact ->
                    ContactAvatar(
                        name = contact.name,
                        size = 36.dp
                    )
                }
            }

            // Contact info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "${group.contacts.size} contacts",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    group.contacts.map { it.name }.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                if (group.contacts.any { it.phoneNumbers.isNotEmpty() }) {
                    Text(
                        group.contacts.firstOrNull { it.phoneNumbers.isNotEmpty() }
                            ?.phoneNumbers?.first() ?: "No phone",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Composable for displaying a contact avatar with initials.
 */
@Composable
fun ContactAvatar(
    name: String,
    size: androidx.compose.ui.unit.Dp = 44.dp
) {
    val initials = name
        .split(" ")
        .take(2)
        .map { it.firstOrNull()?.uppercaseChar() }
        .filterNotNull()
        .joinToString("")

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

/**
 * Composable for selecting master location for merge.
 */
@Composable
fun MasterLocationSelector(
    selectedLocation: String? = null,
    onLocationSelected: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Choose master location",
            style = MaterialTheme.typography.titleMedium
        )

        val locations = listOf("Google", "Device", "SIM")
        locations.forEach { location ->
            LocationChip(
                label = location,
                isSelected = selectedLocation == location,
                onClick = { onLocationSelected(location) }
            )
        }
    }
}

@Composable
private fun LocationChip(
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            label,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}
