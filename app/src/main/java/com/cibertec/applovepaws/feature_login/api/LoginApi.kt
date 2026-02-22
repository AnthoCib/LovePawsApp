package com.cibertec.applovepaws.feature_login.api

import com.cibertec.applovepaws.feature_login.dto.LoginRequestDto
import com.cibertec.applovepaws.feature_login.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): retrofit2.Response<LoginResponseDto>
}