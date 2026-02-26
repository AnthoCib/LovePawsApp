package com.cibertec.applovepaws.feature_user.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: Int,
    val username:String,
    val nombre: String,
    val correo: String,
    val password: String
)