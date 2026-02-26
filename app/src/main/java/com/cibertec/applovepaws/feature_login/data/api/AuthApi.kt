package com.cibertec.applovepaws.feature_login.data.api

import com.cibertec.applovepaws.feature_login.data.dto.LoginRequest
import com.cibertec.applovepaws.feature_login.data.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}