package com.cibertec.applovepaws.feature_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_login.dto.RegisterRequestDto
import com.cibertec.applovepaws.feature_login.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val repo = AuthRepository()

    var loading by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(
        nombre: String,
        correo: String,
        username: String,
        password: String,
        telefono: String,
        direccion: String
    ) {
        viewModelScope.launch {
            loading = true
            errorMessage = null

            try {
                val response = repo.register(
                    RegisterRequestDto(
                        nombre = nombre,
                        correo = correo,
                        username = username,
                        password = password,
                        telefono = telefono,
                        direccion = direccion
                    )
                )

                if (response.isSuccessful) {
                    registerSuccess = true
                } else {
                    errorMessage = "Error al registrar: ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage = "Error de conexión"
            }

            loading = false
        }
    }
}