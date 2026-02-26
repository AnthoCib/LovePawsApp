package com.cibertec.applovepaws.feature_adopcion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_adopcion.data.dto.AdoptionRequestDTO
import com.cibertec.applovepaws.feature_adopcion.data.dto.AdoptionResponseDTO
import kotlinx.coroutines.launch

class AdoptionViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var response by mutableStateOf<AdoptionResponseDTO?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun submit(request: AdoptionRequestDTO) {
        viewModelScope.launch {
            isLoading = true
            error = null
            response = null
            try {
                response = RetrofitClient.lovePawsApi.createAdoption(request)
            } catch (e: Exception) {
                error = e.message ?: "No se pudo registrar la solicitud"
            } finally {
                isLoading = false
            }
        }
    }
}
