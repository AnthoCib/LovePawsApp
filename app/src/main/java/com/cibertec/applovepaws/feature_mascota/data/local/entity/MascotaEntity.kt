package com.cibertec.applovepaws.feature_mascota.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascotas")
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val id: Int? = null,
    val nombre: String,
    val raza: String,
    val edad: Int,
    val sexo: String,
    val descripcion: String,
    val fotoUrl: String,
    val estado: String,
    val sincronizado: Boolean = false
)
