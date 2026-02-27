package com.cibertec.applovepaws.feature_adopcion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_adopcion.data.dto.MascotaRefDto
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SolicitudViewModel(private val repo: AdopcionRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    /** Limpia el mensaje (útil al entrar al formulario para no arrastrar estado anterior) */
    fun limpiarMensaje() {
        _mensaje.value = null
    }

    /** Método simple (compatibilidad): envía solo P2 */
    fun enviarSolicitud(usuarioId: Int, mascotaId: Int, pqAdoptar: String) {
        if (mascotaId <= 0) {
            _mensaje.value = "Mascota inválida"
            return
        }
        if (pqAdoptar.isBlank()) {
            _mensaje.value = "Completa: ¿Por qué deseas adoptarlo?"
            return
        }

        val solicitud = SolicitudAdopcionDto(
            mascota = MascotaRefDto(mascotaId),
            pqAdoptar = pqAdoptar
        )

        enviarSolicitudCompleta(solicitud) // reutiliza el método completo
    }

    /** ✅ Método completo (P1,P2,P3,P4,P5,P6,P13, infoAdicional) */
    fun enviarSolicitudCompleta(solicitud: SolicitudAdopcionDto) {

        // Validación mínima
        if (solicitud.mascota.id <= 0) { _mensaje.value = "Mascota inválida"; return }
        if (solicitud.tiempoDedicado.isNullOrBlank()) { _mensaje.value = "Completa P1"; return }
        if (solicitud.pqAdoptar.isBlank()) { _mensaje.value = "Completa P2"; return }
        if (solicitud.experiencia.isNullOrBlank()) { _mensaje.value = "Completa P3"; return }
        if (solicitud.cubrirCostos.isNullOrBlank()) { _mensaje.value = "Completa P4"; return }
        if (solicitud.tipoVivienda.isNullOrBlank()) { _mensaje.value = "Completa P5"; return }
        if (solicitud.ninosOtraMascotas.isNullOrBlank()) { _mensaje.value = "Completa P6"; return }
        if (solicitud.planMascota.isNullOrBlank()) { _mensaje.value = "Completa P13"; return }

        viewModelScope.launch {
            _isLoading.value = true
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