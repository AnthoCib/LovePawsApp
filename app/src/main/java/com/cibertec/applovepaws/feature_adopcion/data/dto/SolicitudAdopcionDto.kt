package com.cibertec.applovepaws.feature_adopcion.data.dto

data class SolicitudAdopcionDto(
    val id: Int? = null,
    val usuarioId: Int,
    val mascotaId: Int,
    val contactoNombre: String,
    val contactoEmail: String,
    val contactoTelefono: String,
    val tipoVivienda: String,
    val espacioDisponible: String,
    val otrasMascotas: String,
    val motivoAdopcion: String,
    val experienciaMascotas: String,
    val compromisoConfirmado: Boolean
)
