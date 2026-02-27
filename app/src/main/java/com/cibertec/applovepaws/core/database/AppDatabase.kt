package com.cibertec.applovepaws.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cibertec.applovepaws.feature_mascota.data.local.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity
import com.cibertec.applovepaws.feature_user.data.local.dao.UsuarioDao
import com.cibertec.applovepaws.feature_user.data.local.entity.UsuarioEntity


@Database(
    entities = [
        MascotaEntity::class,
        UsuarioEntity::class
    ],version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mascotaDao(): MascotaDao

   abstract fun usuarioDao(): UsuarioDao
}