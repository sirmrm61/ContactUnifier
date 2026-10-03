package com.contactunifier.domain.usecase

import com.contactunifier.data.model.Contact
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching all contacts from all accounts.
 */
class GetContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    operator fun invoke(): Flow<List<Contact>> = contactsRepository.getContactsFromAllAccounts()
}

/**
 * Use case for detecting duplicate contacts.
 * Runs on a background thread and returns groups of potential duplicates.
 */
class DetectDuplicatesUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    operator fun invoke(contacts: List<Contact>): Flow<List<DuplicateGroup>> =
        contactsRepository.detectDuplicates(contacts)
}

/**
 * Use case for merging duplicate contacts.
 * Selects one master contact, merges other contacts into it,
 * and saves to the specified master account location.
 */
class MergeContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    operator fun invoke(
        group: DuplicateGroup,
        masterContact: Contact,
        masterAccountType: String
    ) = contactsRepository.mergeContacts(group, masterContact, masterAccountType)
}

/**
 * Use case for deleting a contact from a specific account.
 */
class DeleteContactUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    operator fun invoke(contact: Contact) = contactsRepository.deleteContact(contact)
}

/**
 * Use case for getting available account types on the device.
 */
class GetAvailableAccountsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    operator fun invoke(): Flow<List<String>> = contactsRepository.getAvailableAccounts()
}
