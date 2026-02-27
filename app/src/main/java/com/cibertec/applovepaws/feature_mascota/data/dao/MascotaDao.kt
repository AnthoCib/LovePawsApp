package com.cibertec.applovepaws.feature_mascota.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity

@Dao
interface MascotaDao {
    @Insert
    suspend fun insertar(mascota: MascotaEntity): Long

    @Query("SELECT * FROM mascotas ORDER BY id DESC")
    suspend fun obtenerTodas(): List<MascotaEntity>

    @Query("SELECT * FROM mascotas WHERE sincronizado = 0 ORDER BY id ASC")
    suspend fun obtenerPendientesSincronizacion(): List<MascotaEntity>

    @Query("UPDATE mascotas SET sincronizado = :sincronizado WHERE id = :id")
    suspend fun actualizarSincronizacion(id: Int, sincronizado: Boolean)
}
