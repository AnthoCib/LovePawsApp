package com.cibertec.applovepaws.core.network

import com.cibertec.applovepaws.feature_adopcion.data.dto.AdoptionRequestDTO
import com.cibertec.applovepaws.feature_adopcion.data.dto.AdoptionResponseDTO
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LovePawsApi {
    @GET("api/mascotas/{id}")
    suspend fun getMascota(@Path("id") id: Long): MascotaDTO

    @POST("api/adopciones")
    suspend fun createAdoption(@Body request: AdoptionRequestDTO): AdoptionResponseDTO
}
