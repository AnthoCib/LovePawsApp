package com.cibertec.applovepaws.feature_mascota.data.api

import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaResponseDTO
import com.cibertec.applovepaws.feature_mascota.data.dto.RegisterMascotaRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MascotaApi {

    @GET("api/mascotas")
    suspend fun listarMascotas(): Response<List<MascotaResponseDTO>>

    @POST("api/mascotas")
    suspend fun registerMascota(@Body request: RegisterMascotaRequestDTO): Response<MascotaResponseDTO>
}
