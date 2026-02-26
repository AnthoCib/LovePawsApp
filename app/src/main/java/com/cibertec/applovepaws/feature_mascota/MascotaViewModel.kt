package com.cibertec.applovepaws.feature_mascota

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.repository.MascotaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MascotaViewModel(
    private val repository: MascotaRepository
) : ViewModel() {

    val mascotas = repository.obtenerMascotas()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    var loading by mutableStateOf(false)
        private set

    init {
        sincronizar()
    }

    private fun sincronizar() {
        viewModelScope.launch {
            loading = true
            repository.sincronizarMascotas()
            loading = false
        }
    }
}