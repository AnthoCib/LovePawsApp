package com.cibertec.applovepaws.feature_mascota

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.repository.MascotaRepository
import kotlinx.coroutines.launch

class MascotaViewModel : ViewModel() {

    private val repo = MascotaRepository()

    var mascotas by mutableStateOf<List<MascotaDto>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    fun cargarMascotas() {
        viewModelScope.launch {
            loading = true

            try {
                mascotas = repo.obtenerMascotas()
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Error")
            }

            loading = false
        }
    }

    fun obtenerMascotaPorId(id: Int): MascotaDto? = mascotas.firstOrNull { it.id == id }
}
