package com.contactunifier.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contactunifier.data.model.Contact
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.domain.usecase.DetectDuplicatesUseCase
import com.contactunifier.domain.usecase.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the main screen showing list of duplicate groups.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val detectDuplicatesUseCase: DetectDuplicatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Idle)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _duplicateGroups = MutableStateFlow<List<DuplicateGroup>>(emptyList())
    val duplicateGroups: StateFlow<List<DuplicateGroup>> = _duplicateGroups.asStateFlow()

    private val _selectedGroups = MutableStateFlow<Set<String>>(emptySet())
    val selectedGroups: StateFlow<Set<String>> = _selectedGroups.asStateFlow()

    fun loadContacts() {
        viewModelScope.launch {
            try {
                _uiState.value = MainUiState.Loading

                getContactsUseCase().collect { contacts ->
                    if (contacts.isNotEmpty()) {
                        detectDuplicatesUseCase(contacts).collect { groups ->
                            _duplicateGroups.value = groups
                            _uiState.value = if (groups.isEmpty()) {
                                MainUiState.NoDuplicatesFound
                            } else {
                                MainUiState.Success
                            }
                        }
                    } else {
                        _uiState.value = MainUiState.NoContactsFound
                    }
                }
            } catch (e: Exception) {
                _uiState.value = MainUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleGroupSelection(groupId: String) {
        val current = _selectedGroups.value.toMutableSet()
        if (current.contains(groupId)) {
            current.remove(groupId)
        } else {
            current.add(groupId)
        }
        _selectedGroups.value = current
    }

    fun clearSelection() {
        _selectedGroups.value = emptySet()
    }
}

sealed class MainUiState {
    object Idle : MainUiState()
    object Loading : MainUiState()
    object Success : MainUiState()
    object NoDuplicatesFound : MainUiState()
    object NoContactsFound : MainUiState()
    data class Error(val message: String) : MainUiState()
}
