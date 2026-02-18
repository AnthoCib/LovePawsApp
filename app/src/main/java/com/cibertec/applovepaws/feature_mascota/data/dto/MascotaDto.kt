package com.cibertec.applovepaws.feature_mascota.data.dto

class MascotaDto(
    val id: Int,
    val nombre: String,
    val edad: Int,
    val sexo: String,
    val descripcion: String?,
    val fotoUrl: String?,

    val categoriaId: Int?,
    val categoriaNombre: String?,

    val razaId: Int?,
    val razaNombre: String?,

    val estadoId: String?,
    val estadoDescripcion: String?,

    val usuarioCreacionId: Int?
)