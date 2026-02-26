package com.cibertec.applovepaws.feature_mascota.data.repository

import com.cibertec.applovepaws.feature_mascota.data.api.MascotaApi
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaRequestDTO
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaResponseDTO
import com.cibertec.applovepaws.feature_mascota.data.dto.toEntity
import com.cibertec.applovepaws.feature_mascota.data.local.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow

class MascotaRepository(
    private val api: MascotaApi,
    private val dao: MascotaDao
) {

    /**
     *  Esta función EXPONE los datos locales (Room).
     *
     * La UI solo observa esta función.
     * No llama directamente a la API.
     *
     * Room devuelve un Flow, por lo tanto:
     * - Si la BD cambia
     * - El Flow emite automáticamente
     * - La UI se actualiza sola
     */
    fun obtenerMascotas(): Flow<List<MascotaEntity>> {
        return dao.obtenerMascotas()
    }

    /**
     *  Esta función sincroniza con el backend (Render).
     *
     * Flujo:
     * API → Convertimos DTO → Guardamos en Room → Flow emite → UI se actualiza
     *
     * Si no hay internet:
     * - No pasa nada
     * - La UI sigue mostrando datos locales
     */
    suspend fun sincronizarMascotas() {
        try {
            val response = api.listarMascotas()
            if (response.isSuccessful) {
                val remote = response.body().orEmpty()
                val entities = remote.map { it.toEntity() }

                dao.limpiarTabla()
                dao.insertarMascotas(entities)
            }
        } catch (e: Exception) {
            // Sin internet → usamos datos locales
        }
    }

    /**
     *  Crear mascota en el servidor
     *
     * Aquí estamos trabajando directamente contra la API.
     * Luego podrías volver a llamar sincronizarMascotas()
     * para actualizar Room.
     */
    suspend fun crearMascota(request: MascotaRequestDTO): Boolean {
        return try {
            val response = api.crear(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun eliminarMascota(id: Int): Boolean {
        return try {
            val response = api.eliminar(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}