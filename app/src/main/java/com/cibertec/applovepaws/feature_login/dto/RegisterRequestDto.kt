package com.cibertec.applovepaws.feature_login.dto

data class RegisterRequestDto(
    val username: String,
    val nombre: String,
    val correo: String,
    val password: String,
    val telefono: String,
    val direccion: String
)