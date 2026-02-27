package com.cibertec.applovepaws.feature_mascota.data.api

import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.dto.RegistrarMascotaRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MascotaApi {

    @GET("api/mascotas")
    suspend fun listarMascotas(): List<MascotaDto>

    @POST("api/mascotas")
    suspend fun registrarMascota(@Body request: RegistrarMascotaRequestDto): MascotaDto
}
