package com.cibertec.applovepaws.feature_mascota.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity

@Dao
interface MascotaDao{
    @Insert
    suspend fun insertar(mascota: MascotaEntity): Long

    @Query("SELECT * FROM mascotas")
    suspend fun obtenerTodas(): List<MascotaEntity>
}