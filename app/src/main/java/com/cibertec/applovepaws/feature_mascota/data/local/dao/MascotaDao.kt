package com.cibertec.applovepaws.feature_mascota.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mascota: MascotaEntity): Long

    @Update
    suspend fun update(mascota: MascotaEntity)

    @Query("SELECT * FROM mascotas ORDER BY localId DESC")
    fun getAll(): Flow<List<MascotaEntity>>

    @Query("SELECT * FROM mascotas WHERE localId = :localId LIMIT 1")
    suspend fun getById(localId: Long): MascotaEntity?

    @Query("SELECT * FROM mascotas WHERE sincronizado = 0 ORDER BY localId ASC")
    suspend fun getMascotasNoSincronizadas(): List<MascotaEntity>
}
