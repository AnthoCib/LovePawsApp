package com.cibertec.applovepaws.feature_adopcion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SolicitudFormData(
    val contactoNombre: String,
    val contactoEmail: String,
    val contactoTelefono: String,
    val tipoVivienda: String,
    val espacioDisponible: String,
    val otrasMascotas: String,
    val motivoAdopcion: String,
    val experienciaMascotas: String,
    val compromisoConfirmado: Boolean
)

class SolicitudViewModel(private val repo: AdopcionRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    fun enviarSolicitud(usuarioId: Int, mascotaId: Int, formData: SolicitudFormData) {
        viewModelScope.launch {
            _isLoading.value = true
            val solicitud = SolicitudAdopcionDto(
                usuarioId = usuarioId,
                mascotaId = mascotaId,
                contactoNombre = formData.contactoNombre,
                contactoEmail = formData.contactoEmail,
                contactoTelefono = formData.contactoTelefono,
                tipoVivienda = formData.tipoVivienda,
                espacioDisponible = formData.espacioDisponible,
                otrasMascotas = formData.otrasMascotas,
                motivoAdopcion = formData.motivoAdopcion,
                experienciaMascotas = formData.experienciaMascotas,
                compromisoConfirmado = formData.compromisoConfirmado
            )
            val result = repo.enviarSolicitud(solicitud)
            _isLoading.value = false

            result.onSuccess {
                _mensaje.value = "Solicitud enviada correctamente"
            }.onFailure {
                _mensaje.value = "Error al enviar: ${it.message}"
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
