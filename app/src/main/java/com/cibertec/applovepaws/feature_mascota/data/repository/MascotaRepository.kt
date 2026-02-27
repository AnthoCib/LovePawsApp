package com.cibertec.applovepaws.feature_mascota.data.repository

import com.cibertec.applovepaws.core.network.RetrofitClient.mascotaApi
import com.cibertec.applovepaws.feature_mascota.data.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity

class MascotaRepository(
    private val dao: MascotaDao? = null
) {

    suspend fun obtenerMascotasRemotas(): List<MascotaDto> {
        return mascotaApi.listarMascotas()
    }

    suspend fun obtenerMascotasLocales(): List<MascotaDto> {
        val localDao = dao ?: return emptyList()
        return localDao.obtenerTodas().map { local ->
            MascotaDto(
                id = -local.id,
                nombre = local.nombre,
                edad = local.edad,
                sexo = local.sexo,
                descripcion = local.descripcion,
                fotoUrl = local.fotoUrl,
                categoriaId = local.categoriaId,
                categoriaNombre = "Local",
                razaId = local.razaId,
                razaNombre = "Local",
                estadoId = local.estadoId,
                estadoDescripcion = local.estadoId,
                usuarioCreacionId = null
            )
        }
    }

    suspend fun registrarMascota(mascota: MascotaEntity) {
        val localDao = dao ?: error("Base de datos local no disponible")
        localDao.insertar(mascota)
    }
}
