package com.cibertec.applovepaws.feature_mascota.data.repository

import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto

class MascotaRepository {
    suspend fun obtenerMascotas(): List<MascotaDto> {
        return RetrofitClient.mascotaApi.listarMascotas()
    }
}