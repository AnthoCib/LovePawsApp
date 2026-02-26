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

class LoginViewModel(private val context: Context): ViewModel() {

    private val repo = AuthRepository()
    var loading by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun login(
        username: String,
        password: String
    ) {

        viewModelScope.launch {

            loading = true
            errorMessage = null

            try {

                val response = repo.login(LoginRequestDto(
                    username = username,
                    password = password
                ))

                if (response.isSuccessful) {

                    val token = response.body()?.token
                    val username = response.body()?.username
                    if (token != null && username != null) {
                        SessionManager.guardarSesion(context, token, username)
                    }
                    loginSuccess = true

                    println("TOKEN: $token")

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