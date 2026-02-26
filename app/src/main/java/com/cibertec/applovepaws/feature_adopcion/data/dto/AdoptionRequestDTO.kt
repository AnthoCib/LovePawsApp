package com.cibertec.applovepaws.feature_adopcion.data.dto

data class AdoptionRequestDTO(
    val mascotaId: Long,
    val solicitanteNombre: String,
    val solicitanteEmail: String,
    val solicitanteTelefono: String,
    val direccion: String,
    val motivo: String
)
