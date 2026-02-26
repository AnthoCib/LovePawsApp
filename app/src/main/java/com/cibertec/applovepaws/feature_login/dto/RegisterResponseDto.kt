package com.cibertec.applovepaws.feature_login.dto

class RegisterResponseDto( // recibes del backend
    val message: String,
    val correo: String,
    val token: String? = null


)