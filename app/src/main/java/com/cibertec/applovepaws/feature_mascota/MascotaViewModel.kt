package com.cibertec.applovepaws.feature_mascota

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaResponseDTO
import kotlinx.coroutines.launch

class MascotaViewModel : ViewModel() {

    var mascotas by mutableStateOf<List<MascotaResponseDTO>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun cargarMascotas() {
        viewModelScope.launch {
            loading = true
            error = null
            try {
                val response = RetrofitClient.mascotaApi.listarMascotas()
                if (response.isSuccessful) {
                    mascotas = response.body().orEmpty()
                } else {
                    error = "Error ${response.code()}"
                }
            } catch (e: Exception) {
                error = e.message ?: "No se pudieron cargar mascotas"
            } finally {
                loading = false
            }
        }
    }
}
