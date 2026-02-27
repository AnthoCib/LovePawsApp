package com.cibertec.applovepaws.feature_mascota.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.cibertec.applovepaws.core.network.RetrofitClient.mascotaApi
import com.cibertec.applovepaws.feature_mascota.data.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.dto.RegistrarMascotaRequestDto
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity
import java.io.File
import java.net.URL
import java.security.MessageDigest

class MascotaRepository(
    private val context: Context,
    private val dao: MascotaDao
) {

    suspend fun obtenerMascotas(): List<MascotaDto> {
        return mascotaApi.listarMascotas()
    }

    suspend fun registrarMascota(mascota: MascotaEntity): String {
        guardarImagenLocalDesdeUrl(mascota.fotoUrl)
        val idLocal = dao.insertar(mascota.copy(sincronizado = false)).toInt()
        if (hayInternet()) {
            val sincronizado = sincronizarMascotaPorId(idLocal)
            return if (sincronizado) {
                "Mascota registrada y sincronizada con backend"
            } else {
                "Mascota guardada localmente; pendiente de sincronización"
            }
        }
        return "Mascota guardada localmente (sin internet)"
    }

    suspend fun sincronizarPendientes(): Int {
        if (!hayInternet()) return 0
        val pendientes = dao.obtenerPendientesSincronizacion()
        var sincronizadas = 0
        pendientes.forEach { pendiente ->
            if (sincronizarMascota(pendiente)) {
                sincronizadas++
            }
        }
        return sincronizadas
    }

    suspend fun obtenerMascotasLocales(): List<MascotaDto> {
        return dao.obtenerTodas().map { entidad ->
            MascotaDto(
                id = entidad.id,
                nombre = entidad.nombre,
                edad = entidad.edad,
                sexo = entidad.sexo,
                descripcion = entidad.descripcion,
                fotoUrl = resolverFotoLocal(entidad.fotoUrl) ?: entidad.fotoUrl,
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

    private suspend fun sincronizarMascotaPorId(idLocal: Int): Boolean {
        val mascotaLocal = dao.obtenerPendientesSincronizacion().firstOrNull { it.id == idLocal } ?: return false
        return sincronizarMascota(mascotaLocal)
    }

    private suspend fun sincronizarMascota(mascotaLocal: MascotaEntity): Boolean {
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
            return true
        } catch (_: Exception) {
            return false
        }
    }

    private fun guardarImagenLocalDesdeUrl(url: String?) {
        if (url.isNullOrBlank() || !url.startsWith("http")) return
        try {
            val carpeta = File(context.filesDir, "mascotas")
            if (!carpeta.exists()) carpeta.mkdirs()
            val nombreArchivo = md5(url) + ".jpg"
            val archivoLocal = File(carpeta, nombreArchivo)
            if (!archivoLocal.exists()) {
                URL(url).openStream().use { input ->
                    archivoLocal.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun resolverFotoLocal(url: String?): String? {
        if (url.isNullOrBlank() || !url.startsWith("http")) return url
        val archivo = File(File(context.filesDir, "mascotas"), md5(url) + ".jpg")
        return if (archivo.exists()) "file://${archivo.absolutePath}" else url
    }

    private fun md5(valor: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val hash = digest.digest(valor.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    private fun hayInternet(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
