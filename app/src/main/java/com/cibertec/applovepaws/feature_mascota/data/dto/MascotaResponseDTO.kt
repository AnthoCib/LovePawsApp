package com.cibertec.applovepaws.feature_mascota.data.dto

import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity


data class MascotaResponseDTO(
    val id: Int,
    val nombre: String,
    val raza: String,
    val categoria: String,
    val edad: Int,
    val sexo: String,
    val descripcion: String?,
    val fotoUrl: String?,
    val estado: String
) {

}
