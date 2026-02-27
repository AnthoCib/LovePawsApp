package com.cibertec.applovepaws.feature_adopcion.data.repository

import com.cibertec.applovepaws.feature_adopcion.data.api.AdoptionApi
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AdopcionRepository(private val api: AdoptionApi) {

    suspend fun enviarSolicitud(request: SolicitudAdopcionDto): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.solicitarAdopcion(request)

                // A veces el backend responde una pantalla HTMLerror  no me vuelvas loco
                val contentType = response.headers()["Content-Type"].orEmpty()

                val rawBody = try {
                    response.body()?.string() ?: response.errorBody()?.string()
                } catch (_: Exception) {
                    null
                }

                val esHtml = contentType.contains("text/html", ignoreCase = true) ||
                        (rawBody?.contains("<!DOCTYPE html", ignoreCase = true) == true)

                if (response.isSuccessful && !esHtml) {
                    Result.success(Unit)
                } else {
                    val msg = extraerMensaje(rawBody)
                        ?: if (!response.isSuccessful)
                            "HTTP ${response.code()} - ${response.message()}"
                        else
                            "El servidor devolvió HTML en vez de JSON"

                    Result.failure(IllegalStateException(msg))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun extraerMensaje(raw: String?): String? {
        if (raw.isNullOrBlank()) return null
        val t = raw.trim()
        if (t.startsWith("{")) {
            runCatching {
                val obj = JSONObject(t)
                when {
                    obj.has("message") -> obj.getString("message")
                    obj.has("error") -> obj.getString("error")
                    else -> null
                }
            }.getOrNull()?.let { if (it.isNotBlank()) return it }
        }
        //
        return Regex(
            "<p[^>]*class=\\\"mb-4\\\"[^>]*>(.*?)</p>",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        ).find(t)
            ?.groupValues
            ?.getOrNull(1)
            ?.replace(Regex("<.*?>"), "")
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }
}