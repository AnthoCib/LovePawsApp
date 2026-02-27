package com.cibertec.applovepaws.feature_mascota.data.repository

import android.content.Context
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.core.network.RetrofitClient.mascotaApi
import com.cibertec.applovepaws.feature_mascota.data.api.MascotaApi
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
}

