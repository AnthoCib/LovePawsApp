package com.cibertec.applovepaws.feature_login.repository

import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_login.api.LoginApi
import com.cibertec.applovepaws.feature_login.dto.LoginRequestDto
import com.cibertec.applovepaws.feature_login.dto.LoginResponseDto
import retrofit2.Response

class LoginRepository {
    private val loginApi = RetrofitClient.loginApi
    suspend fun login(username: String, password: String): Response<LoginResponseDto>{
        return loginApi.login(LoginRequestDto(username, password))
    }
}
