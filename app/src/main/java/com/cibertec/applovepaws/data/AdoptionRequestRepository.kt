package com.cibertec.applovepaws.data

import com.cibertec.applovepaws.BuildConfig
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import com.cibertec.applovepaws.model.AdoptionQuestion
import com.cibertec.applovepaws.model.AdoptionRequest
import kotlinx.coroutines.delay
import java.util.UUID

interface AdoptionRequestRepository {
    suspend fun registerRequest(petId: String, petName: String, applicantName: String, answers: List<AdoptionQuestion>): AdoptionRequest
    suspend fun getRequests(userId: Int): List<AdoptionRequest>
}

class HybridAdoptionRequestRepository : AdoptionRequestRepository {

    private val local = mutableListOf<AdoptionRequest>()

    override suspend fun registerRequest(
        petId: String,
        petName: String,
        applicantName: String,
        answers: List<AdoptionQuestion>
    ): AdoptionRequest {
        val reason = answers.filter { it.checked }.joinToString(separator = " | ") { it.text }
        val payload = SolicitudAdopcionDto(
            usuarioId = 1,
            mascotaId = petId.toIntOrNull() ?: 0,
            pqAdoptar = reason.ifBlank { "Solicitud desde app" },
            infoAdicional = "Solicitante: $applicantName"
        )

        val request = AdoptionRequest(
            id = UUID.randomUUID().toString(),
            petId = petId,
            petName = petName,
            applicantName = applicantName,
            answers = answers
        )

        runCatching { RetrofitClient.adoptionApi.crearSolicitud(payload) }
            .onFailure {
                if (!BuildConfig.USE_FAKE_FALLBACK) throw it
            }

        local.add(request)
        return request
    }

    override suspend fun getRequests(userId: Int): List<AdoptionRequest> {
        val remote = runCatching { RetrofitClient.adoptionApi.listarSolicitudes(userId) }
            .getOrNull()
            ?.map {
                AdoptionRequest(
                    id = it.id?.toString() ?: UUID.randomUUID().toString(),
                    petId = it.mascotaId.toString(),
                    petName = "Mascota #${it.mascotaId}",
                    applicantName = "Usuario #${it.usuarioId}",
                    answers = listOf(AdoptionQuestion("r", it.pqAdoptar, checked = true))
                )
            }

        if (remote != null) return remote

        delay(150)
        return if (BuildConfig.USE_FAKE_FALLBACK) local.toList() else emptyList()
    }
}
