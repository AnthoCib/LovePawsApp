package com.cibertec.applovepaws.feature_adopcion.data.repository

import com.cibertec.applovepaws.feature_adopcion.data.api.AdoptionApi
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto

class AdopcionRepository(private val api: AdoptionApi) {

    suspend fun enviarSolicitud(solicitud: SolicitudAdopcionDto): Result<SolicitudAdopcionDto> {
        return try {
            val response = api.crearSolicitud(solicitud)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
