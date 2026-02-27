package com.cibertec.applovepaws.feature_mascota.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.cibertec.applovepaws.core.network.RetrofitClient.mascotaApi
import com.cibertec.applovepaws.feature_mascota.data.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.dto.RegistrarMascotaRequestDto
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity

class MascotaRepository(
    private val context: Context,
    private val dao: MascotaDao
) {

    suspend fun obtenerMascotas(): List<MascotaDto> {
        return mascotaApi.listarMascotas()
    }

    suspend fun registrarMascota(mascota: MascotaEntity) {
        val idLocal = dao.insertar(mascota.copy(sincronizado = false)).toInt()
        if (hayInternet()) {
            sincronizarMascotaPorId(idLocal)
        }
    }

    suspend fun sincronizarPendientes() {
        if (!hayInternet()) return
        val pendientes = dao.obtenerPendientesSincronizacion()
        pendientes.forEach { pendiente ->
            sincronizarMascota(pendiente)
        }
    }

    suspend fun obtenerMascotasLocales(): List<MascotaDto> {
        return dao.obtenerTodas().map { entidad ->
            MascotaDto(
                id = entidad.id,
                nombre = entidad.nombre,
                edad = entidad.edad,
                sexo = entidad.sexo,
                descripcion = entidad.descripcion,
                fotoUrl = entidad.fotoUrl,
                categoriaId = entidad.categoriaId,
                categoriaNombre = "Local",
                razaId = entidad.razaId,
                razaNombre = "Raza local",
                estadoId = entidad.estadoId,
                estadoDescripcion = if (entidad.sincronizado) "SINCRONIZADO" else "PENDIENTE_SYNC",
                usuarioCreacionId = null
            )
        }
    }

    private suspend fun sincronizarMascotaPorId(idLocal: Int) {
        val mascotaLocal = dao.obtenerPendientesSincronizacion().firstOrNull { it.id == idLocal } ?: return
        sincronizarMascota(mascotaLocal)
    }

    private suspend fun sincronizarMascota(mascotaLocal: MascotaEntity) {
        try {
            mascotaApi.registrarMascota(
                RegistrarMascotaRequestDto(
                    nombre = mascotaLocal.nombre,
                    razaId = mascotaLocal.razaId,
                    categoriaId = mascotaLocal.categoriaId,
                    edad = mascotaLocal.edad,
                    sexo = mascotaLocal.sexo,
                    descripcion = mascotaLocal.descripcion,
                    fotoUrl = mascotaLocal.fotoUrl,
                    estadoId = mascotaLocal.estadoId
                )
            )
            dao.actualizarSincronizacion(mascotaLocal.id, true)
        } catch (_: Exception) {
            // Si falla backend, se mantiene como pendiente para el próximo intento
        }
    }

    private fun hayInternet(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
