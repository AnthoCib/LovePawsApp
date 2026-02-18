package com.cibertec.applovepaws.ui.adopcion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.data.dto.SolicitudAdopcionDto
import com.cibertec.applovepaws.data.repository.AdopcionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SolicitudViewModel(private val repo: AdopcionRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    fun enviarSolicitud(usuarioId: Int, mascotaId: Int, pqAdoptar: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val solicitud = SolicitudAdopcionDto(usuarioId = usuarioId, mascotaId = mascotaId, pqAdoptar = pqAdoptar)
            val result = repo.enviarSolicitud(solicitud)
            _isLoading.value = false

            result.onSuccess {
                _mensaje.value = "Solicitud enviada correctamente"
            }.onFailure {
                _mensaje.value = "Error al enviar: ${it.message}"
            }
        }
    }
}