package com.cibertec.applovepaws.feature_login.dto

data class LoginResponseDto(
    val token: String,
    val tokenType: String,
    val expiresIn: Long,
    val username: String,
    val role: String
)