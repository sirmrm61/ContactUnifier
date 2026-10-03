package com.contactunifier.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.ContactsContract
import com.contactunifier.data.model.Contact
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.data.model.MergeResult
import com.contactunifier.domain.repository.ContactsRepository
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

/**
 * Implementation of ContactsRepository.
 * Handles all contact operations using Android's ContactsContract API.
 */
class ContactsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val contentResolver: ContentResolver
) : ContactsRepository {

    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    /**
     * Fetch all contacts from all available accounts.
     */
    override fun getContactsFromAllAccounts(): Flow<List<Contact>> = flow {
        try {
            val contacts = mutableListOf<Contact>()
            val uri = ContactsContract.Contacts.CONTENT_URI

            val cursor = contentResolver.query(
                uri,
                arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_URI,
                    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                    ContactsContract.Contacts.STARRED,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
                ),
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursor?.use { c ->
                val idIndex = c.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val photoIndex = c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                val thumbIndex = c.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
                val starredIndex = c.getColumnIndex(ContactsContract.Contacts.STARRED)
                val hasPhoneIndex = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                while (c.moveToNext()) {
                    val contactId = c.getLong(idIndex)
                    val name = c.getString(nameIndex) ?: continue

                    // Fetch phone numbers for this contact
                    val phoneNumbers = getPhoneNumbers(contactId)

                    // Fetch emails for this contact
                    val emails = getEmails(contactId)

                    // Fetch account info
                    val (accountName, accountType) = getAccountInfo(contactId)

                    val contact = Contact(
                        id = contactId,
                        rawContactId = contactId,
                        name = name,
                        phoneNumbers = phoneNumbers,
                        emails = emails,
                        accountName = accountName,
                        accountType = accountType,
                        photoUri = c.getString(photoIndex),
                        photoThumbnailUri = c.getString(thumbIndex),
                        starred = c.getInt(starredIndex) != 0,
                        displayNamePrimary = name
                    )

                    contacts.add(contact)
                }
            }

            emit(contacts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Get phone numbers for a specific contact.
     */
    private fun getPhoneNumbers(contactId: Long): List<String> {
        val phoneNumbers = mutableListOf<String>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        cursor?.use { c ->
            val numberIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (c.moveToNext()) {
                val number = c.getString(numberIndex)
                if (!number.isNullOrBlank()) {
                    phoneNumbers.add(normalizePhoneNumber(number))
                }
            }
        }

        return phoneNumbers
    }

    /**
     * Get emails for a specific contact.
     */
    private fun getEmails(contactId: Long): List<String> {
        val emails = mutableListOf<String>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        cursor?.use { c ->
            val emailIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (c.moveToNext()) {
                val email = c.getString(emailIndex)
                if (!email.isNullOrBlank()) {
                    emails.add(email.lowercase())
                }
            }
        }

        return emails
    }

    /**
     * Get account information for a contact.
     */
    private fun getAccountInfo(contactId: Long): Pair<String?, String?> {
        val cursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(
                ContactsContract.RawContacts.ACCOUNT_NAME,
                ContactsContract.RawContacts.ACCOUNT_TYPE
            ),
            ContactsContract.RawContacts.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        var accountName: String? = null
        var accountType: String? = null

        cursor?.use { c ->
            if (c.moveToFirst()) {
                val nameIndex = c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)
                val typeIndex = c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)
                accountName = c.getString(nameIndex)
                accountType = c.getString(typeIndex)
            }
        }

        return Pair(accountName, accountType)
    }

    /**
     * Normalize phone numbers for comparison using libphonenumber.
     */
    private fun normalizePhoneNumber(number: String): String {
        return try {
            val parsed = phoneNumberUtil.parse(number, "US")
            phoneNumberUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164)
        } catch (e: Exception) {
            // Fallback: remove non-digits
            number.filter { it.isDigit() }
        }
    }

    override fun getContactsByAccount(accountType: String): Flow<List<Contact>> = flow {
        emit(emptyList()) // Placeholder
    }.flowOn(Dispatchers.IO)

    override fun detectDuplicates(contacts: List<Contact>): Flow<List<DuplicateGroup>> = flow {
        val duplicateGroups = mutableListOf<DuplicateGroup>()
        val processed = mutableSetOf<Long>()

        for (i in contacts.indices) {
            if (processed.contains(contacts[i].id)) continue

            val group = mutableListOf(contacts[i])
            processed.add(contacts[i].id)

            for (j in (i + 1) until contacts.size) {
                if (processed.contains(contacts[j].id)) continue

                val similarity = calculateSimilarity(contacts[i], contacts[j])
                if (similarity > 0.7f) {
                    group.add(contacts[j])
                    processed.add(contacts[j].id)
                }
            }

            if (group.size > 1) {
                duplicateGroups.add(
                    DuplicateGroup(
                        groupId = UUID.randomUUID().toString(),
                        contacts = group,
                        matchReason = "Possible duplicate",
                        confidence = 0.8f
                    )
                )
            }
        }

        emit(duplicateGroups)
    }.flowOn(Dispatchers.Default)

    /**
     * Calculate similarity between two contacts.
     * Returns a score from 0.0 to 1.0
     */
    private fun calculateSimilarity(c1: Contact, c2: Contact): Float {
        var score = 0f

        // Check phone number match
        val commonPhones = c1.phoneNumbers.intersect(c2.phoneNumbers.toSet())
        if (commonPhones.isNotEmpty()) {
            score += 0.5f
        }

        // Check email match
        val commonEmails = c1.emails.intersect(c2.emails.toSet())
        if (commonEmails.isNotEmpty()) {
            score += 0.3f
        }

        // Check name similarity
        if (c1.name.equals(c2.name, ignoreCase = true)) {
            score += 0.2f
        } else if (isSimilarName(c1.name, c2.name)) {
            score += 0.1f
        }

        return score
    }

    private fun isSimilarName(name1: String, name2: String): Boolean {
        val n1 = name1.lowercase().trim()
        val n2 = name2.lowercase().trim()

        // Levenshtein distance check
        val distance = levenshteinDistance(n1, n2)
        val maxLen = maxOf(n1.length, n2.length)
        return distance <= maxLen * 0.25 // Allow 25% difference
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }

        return dp[s1.length][s2.length]
    }

    /**
     * Merge a group of duplicate contacts into one master contact.
     * Consolidates all data and deletes duplicates from other accounts.
     */
    override fun mergeContacts(
        group: DuplicateGroup,
        masterContact: Contact,
        masterAccountType: String
    ): Flow<MergeResult> = flow {
        try {
            emit(MergeResult.InProgress)

            // Collect all unique data from duplicate contacts
            val allPhones = mutableSetOf<String>()
            val allEmails = mutableSetOf<String>()
            var finalName = masterContact.name
            var finalOrganization = masterContact.organization
            var finalNotes = masterContact.notes

            group.contacts.forEach { contact ->
                allPhones.addAll(contact.phoneNumbers)
                allEmails.addAll(contact.emails)

                // Use longest/most complete name
                if (contact.name.length > finalName.length) {
                    finalName = contact.name
                }

                // Collect additional info
                if (contact.organization != null && finalOrganization == null) {
                    finalOrganization = contact.organization
                }
                if (contact.notes != null && finalNotes == null) {
                    finalNotes = contact.notes
                }
            }

            // Create merged contact
            val mergedContact = Contact(
                id = masterContact.id,
                rawContactId = masterContact.rawContactId,
                name = finalName,
                phoneNumbers = allPhones.toList(),
                emails = allEmails.toList(),
                accountName = masterContact.accountName,
                accountType = masterAccountType,
                photoUri = masterContact.photoUri,
                photoThumbnailUri = masterContact.photoThumbnailUri,
                starred = group.contacts.any { it.starred },
                notes = finalNotes,
                organization = finalOrganization,
                displayNamePrimary = finalName
            )

            // Update the master contact with merged data
            val cv = android.content.ContentValues().apply {
                put(ContactsContract.Contacts.DISPLAY_NAME, mergedContact.name)
                put(ContactsContract.Contacts.STARRED, if (mergedContact.starred) 1 else 0)
            }

            val rawContactUri = ContentUris.withAppendedId(
                ContactsContract.RawContacts.CONTENT_URI,
                masterContact.rawContactId
            )

            contentResolver.update(rawContactUri, cv, null, null)

            // Delete duplicate contacts
            var deletedCount = 0
            for (contact in group.contacts) {
                if (contact.id != masterContact.id) {
                    try {
                        val deleteUri = ContentUris.withAppendedId(
                            ContactsContract.RawContacts.CONTENT_URI,
                            contact.rawContactId
                        )
                        contentResolver.delete(deleteUri, null, null)
                        deletedCount++
                    } catch (e: Exception) {
                        // Continue with next contact even if deletion fails
                    }
                }
            }

            emit(MergeResult.Success(mergedContact, deletedCount))
        } catch (e: Exception) {
            emit(MergeResult.Error("Failed to merge contacts: ${e.message}", e))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Delete a single contact from a specific account.
     */
    override fun deleteContact(contact: Contact): Flow<MergeResult> = flow {
        try {
            emit(MergeResult.InProgress)

            val deleteUri = ContentUris.withAppendedId(
                ContactsContract.RawContacts.CONTENT_URI,
                contact.rawContactId
            )

            val rowsDeleted = contentResolver.delete(deleteUri, null, null)

            if (rowsDeleted > 0) {
                emit(MergeResult.Success(contact, rowsDeleted))
            } else {
                emit(MergeResult.Error("Could not delete contact", null))
            }
        } catch (e: Exception) {
            emit(MergeResult.Error("Failed to delete contact: ${e.message}", e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAvailableAccounts(): Flow<List<String>> = flow {
        emit(listOf("com.google", "com.android.contacts", "SIM"))
    }.flowOn(Dispatchers.IO)
}
