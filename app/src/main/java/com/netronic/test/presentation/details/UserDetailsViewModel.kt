package com.netronic.test.presentation.details



import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netronic.test.domain.model.User
import com.netronic.test.domain.usecase.GetUserByIdUseCase
import com.netronic.test.logging.AppLogger
import com.netronic.test.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getUserById: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object { const val TAG = "VM.Details" }

    private val userId: String = checkNotNull(savedStateHandle["userId"]).also{
        AppLogger.d(TAG, "init | userId=$it")
    } as String

    private val _state = MutableStateFlow<UiState<User>>(UiState.Loading)
    val state: StateFlow<UiState<User>> = _state


    init {
        viewModelScope.launch {
            try {
                val user = getUserById(userId)
                if (user != null) _state.value = UiState.Success(user)
                else _state.value = UiState.Error("User not found")
            } catch (t: Throwable) {
                _state.value = UiState.Error(t.message ?: "Unknown error")
            }
        }
    }
}