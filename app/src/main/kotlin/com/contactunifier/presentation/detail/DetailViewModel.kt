package com.contactunifier.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contactunifier.data.model.Contact
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.data.model.MergeResult
import com.contactunifier.domain.usecase.MergeContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the detail/merge screen.
 * Handles merge operations and master location selection.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mergeContactsUseCase: MergeContactsUseCase
) : ViewModel() {

    private val _selectedMasterLocation = MutableStateFlow<String?>(null)
    val selectedMasterLocation: StateFlow<String?> = _selectedMasterLocation.asStateFlow()

    private val _mergeState = MutableStateFlow<MergeState>(MergeState.Idle)
    val mergeState: StateFlow<MergeState> = _mergeState.asStateFlow()

    private val _selectedMasterContact = MutableStateFlow<Contact?>(null)
    val selectedMasterContact: StateFlow<Contact?> = _selectedMasterContact.asStateFlow()

    fun setMasterLocation(location: String) {
        _selectedMasterLocation.value = location
    }

    fun setMasterContact(contact: Contact) {
        _selectedMasterContact.value = contact
    }

    fun mergeDuplicateGroup(group: DuplicateGroup) {
        val masterContact = _selectedMasterContact.value ?: return
        val masterLocation = _selectedMasterLocation.value ?: return

        viewModelScope.launch {
            try {
                _mergeState.value = MergeState.Merging

                mergeContactsUseCase(
                    group = group,
                    masterContact = masterContact,
                    masterAccountType = masterLocation
                ).collect { result ->
                    when (result) {
                        is MergeResult.Success -> {
                            _mergeState.value = MergeState.Success(
                                mergedContact = result.mergedContact,
                                deletedCount = result.deletedCount
                            )
                        }
                        is MergeResult.Error -> {
                            _mergeState.value = MergeState.Error(result.message)
                        }
                        MergeResult.InProgress -> {
                            _mergeState.value = MergeState.Merging
                        }
                    }
                }
            } catch (e: Exception) {
                _mergeState.value = MergeState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetMergeState() {
        _mergeState.value = MergeState.Idle
        _selectedMasterLocation.value = null
        _selectedMasterContact.value = null
    }
}

sealed class MergeState {
    object Idle : MergeState()
    object Merging : MergeState()
    data class Success(val mergedContact: Contact, val deletedCount: Int) : MergeState()
    data class Error(val message: String) : MergeState()
}
