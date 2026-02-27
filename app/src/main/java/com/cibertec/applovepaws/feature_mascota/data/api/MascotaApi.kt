package com.cibertec.applovepaws.feature_mascota.data.api

import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaRequestDTO
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MascotaApi {


    @GET("api/mascotas")
    suspend fun listarMascotas(): Response<List<MascotaResponseDTO>>

    @GET("api/mascotas/disponibles")
    suspend fun listarDisponibles(): Response<List<MascotaResponseDTO>>

    @GET("api/mascotas/{id}")
    suspend fun detalle(@Path("id") id: Int): Response<MascotaResponseDTO>

    @POST("api/mascotas")
    suspend fun crear(@Body request: MascotaRequestDTO): Response<MascotaResponseDTO>

    @PUT("api/mascotas/{id}")
    suspend fun actualizar(
        @Path("id") id: Int,
        @Body request: MascotaRequestDTO
    ): Response<MascotaResponseDTO>

    @DELETE("api/mascotas/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}