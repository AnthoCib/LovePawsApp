package com.cibertec.applovepaws.feature_adopcion.data.dto

class SolicitudAdopcionDto (    val id: Int? = null,
                                val usuarioId: Int,
                                val mascotaId: Int,
                                val pqAdoptar: String,
                                val infoAdicional: String? = null){

}