package com.cibertec.applovepaws.feature_login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.core.session.SessionManager
import com.cibertec.applovepaws.feature_login.dto.LoginRequestDto
import com.cibertec.applovepaws.feature_login.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val context: Context) : ViewModel() {

    private val repo = AuthRepository()
    var loading by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf(false)

    var successMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(
        username: String,
        password: String,
        rolEsperado: String
    ) {

        viewModelScope.launch {

            loading = true
            errorMessage = null
            successMessage = null

            try {

                val response = repo.login(
                    LoginRequestDto(
                        username = username,
                        password = password
                    )
                )

                if (response.isSuccessful) {

                    val body = response.body()
                    val token = body?.token
                    val usernameResponse = body?.username
                    val role = body?.role

                    if (token != null && usernameResponse != null && role != null) {
                        if (!role.equals(rolEsperado, ignoreCase = true)) {
                            SessionManager.cerrarSesion(context)
                            errorMessage = "Tu usuario tiene rol $role. Selecciona el rol correcto para ingresar."
                        } else {
                            SessionManager.guardarSesion(context, token, usernameResponse, role)
                            successMessage = "Login correcto como $role"
                            loginSuccess = true
                        }
                    } else {
                        errorMessage = "Respuesta inválida del servidor"
                    }

                } else {
                    errorMessage = "Credenciales incorrectas"
                }

            } catch (e: Exception) {
                errorMessage = "Error de conexión"
            }

            loading = false
        }
    }
}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(context) as T
    }
}
