package com.cibertec.applovepaws.feature_login.data.repository

import com.cibertec.applovepaws.core.utils.TokenManager
import com.cibertec.applovepaws.feature_login.data.api.AuthApi
import com.cibertec.applovepaws.feature_login.data.dto.LoginRequest

class AuthRepository(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {

            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                response.body()?.let {
                    tokenManager.saveToken(it.token)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}