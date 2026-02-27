package com.cibertec.applovepaws.feature_mascota.data.repository

import com.cibertec.applovepaws.core.network.RetrofitClient.mascotaApi
import com.cibertec.applovepaws.feature_mascota.data.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity

class MascotaRepository(
    private val dao: MascotaDao
){

    //Obtiene el mascotaApi del Retrofit
    suspend fun obtenerMascotas(): List<MascotaDto> {
        val result = mascotaApi.listarMascotas()
        println("MASCOTAS -> $result")
        return result
       // return RetrofitClient.mascotaApi.listarMascotas()
    }

    suspend fun registrarMascota(mascota: MascotaEntity) {
        dao.insertar(mascota)
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
                estadoDescripcion = entidad.estadoId ?: "SIN ESTADO",
                usuarioCreacionId = null
            )
        }
    }
}
