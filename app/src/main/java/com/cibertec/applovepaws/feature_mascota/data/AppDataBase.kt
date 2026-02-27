package com.cibertec.applovepaws.feature_mascota.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cibertec.applovepaws.feature_mascota.data.dao.MascotaDao
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity

@Database(entities = [MascotaEntity::class], version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun mascotaDao(): MascotaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "lovepaws_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
