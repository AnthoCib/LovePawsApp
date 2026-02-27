package com.cibertec.applovepaws.feature_mascota.data.dto

import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity

fun MascotaResponseDTO.toEntity(): MascotaEntity {
    return MascotaEntity(
        id = id,
        nombre = nombre,
        raza = raza,
        edad = edad,
        sexo = sexo,
        descripcion = descripcion.orEmpty(),
        fotoUrl = fotoUrl.orEmpty(),
        estado = estado,
        sincronizado = true
    )
}
