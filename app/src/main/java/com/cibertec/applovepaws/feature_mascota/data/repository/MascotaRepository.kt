package com.cibertec.applovepaws.feature_mascota.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.cibertec.applovepaws.feature_mascota.data.api.MascotaApi
import com.cibertec.applovepaws.feature_mascota.data.dto.RegisterMascotaRequestDTO
import com.cibertec.applovepaws.feature_mascota.data.local.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow

class MascotaRepository(
    private val context: Context,
    private val api: MascotaApi,
    private val dao: MascotaDao
) {

    fun getMascotas(): Flow<List<MascotaEntity>> = dao.getAll()

    suspend fun guardarLocal(mascota: MascotaEntity): Long {
        return dao.insert(mascota.copy(sincronizado = false))
    }

    suspend fun registrarMascota(mascota: MascotaEntity): Result {
        val localId = guardarLocal(mascota)
        val guardada = dao.getById(localId) ?: return Result.Error("No se pudo guardar localmente")

        if (!verificarConexion()) {
            return Result.Success("Mascota guardada localmente. Se sincronizará cuando vuelva internet")
        }

        return try {
            val response = api.registerMascota(guardada.toRequest())
            if (response.isSuccessful) {
                val remoteId = response.body()?.id
                dao.update(guardada.copy(id = remoteId, sincronizado = true))
                Result.Success("Mascota registrada y sincronizada")
            } else {
                Result.Error("Mascota guardada localmente. Falló sincronización con API")
            }
        } catch (_: Exception) {
            Result.Error("Mascota guardada localmente. Sincronización pendiente")
        }
    }

    suspend fun sincronizarPendientes() {
        if (!verificarConexion()) return

        val pendientes = dao.getMascotasNoSincronizadas()
        pendientes.forEach { mascota ->
            try {
                val response = api.registerMascota(mascota.toRequest())
                if (response.isSuccessful) {
                    dao.update(
                        mascota.copy(
                            id = response.body()?.id,
                            sincronizado = true
                        )
                    )
                }
            } catch (_: Exception) {
                // Se mantiene sincronizado = false para el próximo intento.
            }
        }
    }

    fun verificarConexion(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}

sealed class Result {
    data class Success(val message: String) : Result()
    data class Error(val message: String) : Result()
}

private fun MascotaEntity.toRequest(): RegisterMascotaRequestDTO {
    return RegisterMascotaRequestDTO(
        nombre = nombre,
        raza = raza,
        edad = edad,
        sexo = sexo,
        descripcion = descripcion,
        fotoUrl = fotoUrl,
        estado = estado
    )
}
