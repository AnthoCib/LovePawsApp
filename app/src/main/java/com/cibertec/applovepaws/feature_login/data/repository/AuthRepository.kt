package com.cibertec.applovepaws.feature_login.data.repository

import com.cibertec.applovepaws.core.utils.TokenManager
import com.cibertec.applovepaws.feature_login.data.api.AuthApi
import com.cibertec.applovepaws.feature_login.data.dto.LoginRequest
import com.cibertec.applovepaws.feature_login.data.dto.LoginResponse

class AuthRepository(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenManager.saveToken(body.token)
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}
