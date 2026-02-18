package com.cibertec.applovepaws.feature_adoption.data.api

import com.cibertec.applovepaws.feature_adoption.data.dto.SolicitudAdopcionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AdoptionApi {
    @POST("api/solicitudes")
     fun crearSolicitud(
        @Body solicitud: SolicitudAdopcionDto
    ): Response<SolicitudAdopcionDto>
}