package com.cibertec.applovepaws.feature_login.repository

import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_login.dto.LoginRequestDto
import com.cibertec.applovepaws.feature_login.dto.LoginResponseDto
import com.cibertec.applovepaws.feature_login.dto.RegisterRequestDto
import com.cibertec.applovepaws.feature_login.dto.RegisterResponseDto
import okhttp3.Request
import retrofit2.Response

class AuthRepository {
    private val api = RetrofitClient.authApiService
    suspend fun login(request: LoginRequestDto): Response<LoginResponseDto>{
        return api.login(request)
    }

    suspend fun register(request: RegisterRequestDto): Response<RegisterResponseDto>{
        return api.register(request)
    }
}
