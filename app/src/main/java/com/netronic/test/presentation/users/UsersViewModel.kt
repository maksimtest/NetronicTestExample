package com.netronic.test.presentation.users


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netronic.test.domain.model.User
import com.netronic.test.domain.usecase.GetUsersUseCase
import com.netronic.test.logging.AppLogger
import com.netronic.test.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsers: GetUsersUseCase
) : ViewModel() {

    private companion object {
        const val TAG = "VM.Users"
    }

    private val _state = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val state: StateFlow<UiState<List<User>>> = _state


    init {
        refresh()
    }


    //    fun refresh() = viewModelScope.launch {
//        _state.value = UiState.Loading
//        try {
//            val users = getUsers()
//            _state.value = UiState.Success(users)
//        } catch (t: Throwable) {
//            _state.value = UiState.Error(t.message ?: "Unknown error")
//        }
//    }
    fun refresh() = viewModelScope.launch {
        AppLogger.d(TAG, "refresh() | start")
        _state.value = UiState.Loading
        try {
            val users = getUsers()
            AppLogger.d(TAG, "refresh() | success count=${users.size}")
            _state.value = UiState.Success(users)
        } catch (t: Throwable) {
            AppLogger.e(TAG, "refresh() | error=${t.message}", t)
            _state.value = UiState.Error(t.message ?: "Unknown error")
        }
    }
}