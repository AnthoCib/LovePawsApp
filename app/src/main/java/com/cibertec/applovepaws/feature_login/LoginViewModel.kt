package com.cibertec.applovepaws.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_login.data.dto.LoginResponse
import com.cibertec.applovepaws.feature_login.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val user: LoginResponse? = null
)

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = repository.login(email, password)

            result.onSuccess { user ->
                _uiState.value = AuthUiState(
                    isSuccess = true,
                    user = user
                )
            }.onFailure { error ->
                _uiState.value = AuthUiState(
                    error = error.message ?: "Credenciales inválidas"
                )
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        _uiState.value = AuthUiState(
            error = "Registro no implementado en esta versión"
        )
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isSuccess = false,
            error = null
        )
    }
}
