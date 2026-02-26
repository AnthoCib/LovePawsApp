package com.cibertec.applovepaws.feature_mascota.data.dto

data class MascotaDTO(
    val id: Long,
    val nombre: String,
    val razaId: Long,
    val categoriaId: Long,
    val edad: Int,
    val sexo: String,
    val descripcion: String?,
    val fotoUrl: String?,
    val estadoId: Long
)
