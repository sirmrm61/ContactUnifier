package com.contactunifier.domain.repository

import com.contactunifier.data.model.Contact
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.data.model.MergeResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for contact operations.
 * Abstraction layer between data and domain layers.
 */
interface ContactsRepository {
    /**
     * Fetch all contacts from all available accounts (SIM, Google, Device).
     * Emits loading progress as a percentage (0-100).
     */
    fun getContactsFromAllAccounts(): Flow<List<Contact>>

    /**
     * Get all contacts with a specific account type.
     */
    fun getContactsByAccount(accountType: String): Flow<List<Contact>>

    /**
     * Detect duplicate contacts across all accounts.
     * Returns groups of potential duplicates.
     */
    fun detectDuplicates(contacts: List<Contact>): Flow<List<DuplicateGroup>>

    /**
     * Merge a group of duplicate contacts into one.
     * Saves the merged contact to the specified master account.
     * Deletes duplicates from other accounts.
     */
    fun mergeContacts(
        group: DuplicateGroup,
        masterContact: Contact,
        masterAccountType: String
    ): Flow<MergeResult>

    /**
     * Delete a contact from a specific account.
     */
    fun deleteContact(contact: Contact): Flow<MergeResult>

    /**
     * Get available account types on the device.
     */
    fun getAvailableAccounts(): Flow<List<String>>
}
