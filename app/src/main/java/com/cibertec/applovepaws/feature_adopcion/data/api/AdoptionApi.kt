package com.cibertec.applovepaws.feature_adopcion.data.api

import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AdoptionApi {

    @POST("api/adopciones/solicitar")
    suspend fun solicitarAdopcion(
        @Body solicitud: SolicitudAdopcionDto
    ): Response<Unit>
}