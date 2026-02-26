package com.cibertec.applovepaws.feature_mascota

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDTO
import kotlinx.coroutines.launch

class MascotaDetailViewModel : ViewModel() {
    var mascota by mutableStateOf<MascotaDTO?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun loadMascota(id: Long) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                mascota = RetrofitClient.lovePawsApi.getMascota(id)
            } catch (e: Exception) {
                error = e.message ?: "No se pudo cargar el detalle"
            } finally {
                isLoading = false
            }
        }
    }
}
