package com.cibertec.applovepaws.feature_mascota.data.api

import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import retrofit2.http.GET

interface MascotaApi {


    @GET("api/mascotas")
    suspend fun obtenerMascotas(): List<MascotaDto>
}