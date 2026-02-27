package com.cibertec.applovepaws.feature_mascota.data.dto

data class RegisterMascotaRequestDTO(
    val nombre: String,
    val raza: String,
    val edad: Int,
    val sexo: String,
    val descripcion: String,
    val fotoUrl: String,
    val estado: String
)
