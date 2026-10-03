package com.contactunifier.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a single contact with all its details.
 * Can be from SIM, Google account, or device storage.
 */
@Parcelize
data class Contact(
    val id: Long,
    val rawContactId: Long,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList(),
    val accountName: String? = null,
    val accountType: String? = null, // e.g., "com.google", "com.android.contacts", "SIM"
    val photoUri: String? = null,
    val photoThumbnailUri: String? = null,
    val starred: Boolean = false,
    val notes: String? = null,
    val organization: String? = null,
    val displayNamePrimary: String = name
) : Parcelable

/**
 * Represents a group of duplicate contacts.
 */
@Parcelize
data class DuplicateGroup(
    val groupId: String,
    val contacts: List<Contact>,
    val matchReason: String, // e.g., "Same phone number", "Similar name"
    val confidence: Float = 0.8f // 0.0 to 1.0
) : Parcelable

/**
 * Result of a merge operation.
 */
sealed class MergeResult {
    data class Success(val mergedContact: Contact, val deletedCount: Int) : MergeResult()
    data class Error(val message: String, val exception: Exception? = null) : MergeResult()
    object InProgress : MergeResult()
}
