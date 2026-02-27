package com.cibertec.applovepaws.feature_mascota.data.dto

data class RegistrarMascotaRequestDto(
    val nombre: String,
    val razaId: Int,
    val categoriaId: Int,
    val edad: Int,
    val sexo: String,
    val descripcion: String?,
    val fotoUrl: String?,
    val estadoId: String?
)
