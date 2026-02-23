package com.cibertec.applovepaws.feature_login.data.dto

class LoginResponse(
    val token: String,
    val id: Long,
    val name: String,
    val email: String
) {
}