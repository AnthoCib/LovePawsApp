package com.cibertec.applovepaws.feature_mascota.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascotas")
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val razaId: Int,
    val categoriaId: Int,
    val edad: Int,
    val sexo: String,
    val descripcion: String?,
    val fotoUrl: String?,
    val estadoId: String?,
    val sincronizado: Boolean = false
)
