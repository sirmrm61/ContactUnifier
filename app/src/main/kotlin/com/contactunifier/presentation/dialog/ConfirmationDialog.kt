package com.contactunifier.presentation.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.contactunifier.data.model.Contact
import com.contactunifier.data.model.DuplicateGroup

/**
 * Confirmation dialog for merge operations.
 * Shows which contacts will be deleted and requests user confirmation.
 */
@Composable
fun MergeConfirmationDialog(
    group: DuplicateGroup,
    masterLocation: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val contactsToDelete = group.contacts.size - 1
    val masterContact = group.contacts.first()
    val otherContacts = group.contacts.drop(1)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Confirm Merge",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                buildString {
                    appendLine("Merge the following contacts:")
                    appendLine()
                    group.contacts.forEach { contact ->
                        appendLine("• ${contact.name}")
                        if (contact.phoneNumbers.isNotEmpty()) {
                            appendLine("  ${contact.phoneNumbers.first()}")
                        }
                    }
                    appendLine()
                    appendLine("Master contact: ${masterContact.name}")
                    appendLine("Stored in: $masterLocation")
                    appendLine()
                    appendLine("$contactsToDelete duplicate contact(s) will be deleted.")
                },
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes, Merge")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Confirmation dialog for deleting a contact.
 */
@Composable
fun DeleteConfirmationDialog(
    contact: Contact,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Delete Contact?",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                "Delete '${contact.name}' from ${contact.accountType ?: "device"}?\n\nThis action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Success dialog after merge operation.
 */
@Composable
fun MergeSuccessDialog(
    contactCount: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Merge Successful!",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                "Successfully merged $contactCount contacts into one.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
