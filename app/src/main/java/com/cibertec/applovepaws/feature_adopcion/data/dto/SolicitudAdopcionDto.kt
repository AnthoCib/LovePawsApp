package com.cibertec.applovepaws.feature_adopcion.data.dto

data class SolicitudAdopcionDto(
    val id: Int? = null,
    val mascota: MascotaRefDto,
    val pqAdoptar: String,
    val tiempoDedicado: String? = null,
    val cubrirCostos: String? = null,
    val planMascota: String? = null,
    val tipoVivienda: String? = null,
    val experiencia: String? = null,
    val ninosOtraMascotas: String? = null,
    val infoAdicional: String? = null,
    val estado: EstadoRefDto = EstadoRefDto(id = "PENDIENTE")
)

data class MascotaRefDto(val id: Int)
data class EstadoRefDto(val id: String)