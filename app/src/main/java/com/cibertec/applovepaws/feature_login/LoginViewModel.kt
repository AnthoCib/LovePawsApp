package com.cibertec.applovepaws.feature_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_login.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val repo = LoginRepository()
    var loading by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(username: String, password: String) {

        viewModelScope.launch {

            loading = true
            errorMessage = null

            try {

                val response = repo.login(username, password)

                if (response.isSuccessful) {

                    val token = response.body()?.token
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