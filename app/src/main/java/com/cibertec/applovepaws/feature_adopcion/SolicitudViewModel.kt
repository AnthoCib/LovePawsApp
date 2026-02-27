package com.cibertec.applovepaws.feature_adopcion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SolicitudViewModel(
    private val repository: AdopcionRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun enviarSolicitudCompleta(dto: SolicitudAdopcionDto) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _mensaje.value = null

            val result = repository.enviarSolicitud(dto)

            _isLoading.value = false

            result
                .onSuccess {
                    _mensaje.value = "Solicitud enviada correctamente"
                }
                .onFailure { e ->
                    val raw = (e.message ?: "Ocurrió un error").trim()

                    // si el server ya dijo “solicitud activa”, mostramos eso tal cual (sin ruido)
                    val friendly =
                        if (raw.contains("solicitud activa", ignoreCase = true)) raw
                        else if (raw.contains("ya tiene", ignoreCase = true) && raw.contains("solicitud", ignoreCase = true)) raw
                        else raw

                    _mensaje.value = friendly
                }
        }
    }
}