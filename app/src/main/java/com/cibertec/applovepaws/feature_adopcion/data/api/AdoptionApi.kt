package com.cibertec.applovepaws.feature_adopcion.data.api

import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AdoptionApi {
    @POST("api/solicitudes")
    suspend fun crearSolicitud(
        @Body solicitud: SolicitudAdopcionDto
    ): Response<SolicitudAdopcionDto>

    @GET("api/solicitudes")
    suspend fun listarSolicitudes(
        @Query("usuarioId") usuarioId: Int
    ): List<SolicitudAdopcionDto>
}
