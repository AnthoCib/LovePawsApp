package com.cibertec.applovepaws.feature_login.api

import com.cibertec.applovepaws.feature_login.dto.LoginRequestDto
import com.cibertec.applovepaws.feature_login.dto.LoginResponseDto
import com.cibertec.applovepaws.feature_login.dto.RegisterRequestDto
import com.cibertec.applovepaws.feature_login.dto.RegisterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): retrofit2.Response<LoginResponseDto>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<RegisterResponseDto>
}