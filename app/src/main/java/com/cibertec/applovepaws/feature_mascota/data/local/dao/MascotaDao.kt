package com.cibertec.applovepaws.feature_mascota.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface  MascotaDao {
    @Query("SELECT * FROM mascotas")
    fun obtenerMascotas(): Flow<List<MascotaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMascotas(lista: List<MascotaEntity>)

    @Query("DELETE FROM mascotas")
    suspend fun limpiarTabla()


}

