package com.cibertec.applovepaws.feature_mascota.data.dto

data class MascotaRequestDTO (
    val nombre: String,
    val razaId: Int,
    val categoriaId: Int,
    val edad: Int,
    val sexo: String,        // "M" o "H"
    val descripcion: String?,
    val fotoUrl: String?,
    val estadoId: Int
){

}