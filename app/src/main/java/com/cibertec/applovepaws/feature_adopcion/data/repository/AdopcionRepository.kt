package com.cibertec.applovepaws.feature_adopcion.data.repository

import com.cibertec.applovepaws.feature_adopcion.data.api.AdoptionApi
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdopcionRepository(private val api: AdoptionApi) {

    suspend fun enviarSolicitud(request: SolicitudAdopcionDto): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.solicitarAdopcion(request)
                if (response.isSuccessful) Result.success(Unit)
                else Result.failure(Exception("Error ${response.code()} - ${response.message()}"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}