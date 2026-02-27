package com.cibertec.applovepaws.feature_mascota

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.feature_mascota.data.local.entity.MascotaEntity
import com.cibertec.applovepaws.feature_mascota.data.repository.MascotaRepository
import com.cibertec.applovepaws.feature_mascota.data.repository.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MascotaFormState(
    val nombre: String = "",
    val raza: String = "",
    val edad: String = "",
    val sexo: String = "",
    val descripcion: String = "",
    val fotoUrl: String = "",
    val estado: String = ""
)

class MascotaViewModel(
    private val repository: MascotaRepository,
    private val currentRole: String
) : ViewModel() {

    val mascotas = repository.getMascotas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    var formState by mutableStateOf(MascotaFormState())
        private set

    var loading by mutableStateOf(false)
        private set

    var message by mutableStateOf<String?>(null)
        private set

    val isGestor: Boolean
        get() = currentRole.equals("GESTOR", ignoreCase = true)

    fun onNombreChange(value: String) { formState = formState.copy(nombre = value) }
    fun onRazaChange(value: String) { formState = formState.copy(raza = value) }
    fun onEdadChange(value: String) { formState = formState.copy(edad = value) }
    fun onSexoChange(value: String) { formState = formState.copy(sexo = value) }
    fun onDescripcionChange(value: String) { formState = formState.copy(descripcion = value) }
    fun onFotoUrlChange(value: String) { formState = formState.copy(fotoUrl = value) }
    fun onEstadoChange(value: String) { formState = formState.copy(estado = value) }

    fun registrarMascota() {
        if (!isGestor) {
            message = "Solo los gestores pueden registrar mascotas"
            return
        }

        val validation = validarCampos()
        if (validation != null) {
            message = validation
            return
        }

        viewModelScope.launch {
            loading = true
            val mascota = MascotaEntity(
                nombre = formState.nombre.trim(),
                raza = formState.raza.trim(),
                edad = formState.edad.toInt(),
                sexo = formState.sexo.trim(),
                descripcion = formState.descripcion.trim(),
                fotoUrl = formState.fotoUrl.trim(),
                estado = formState.estado.trim(),
                sincronizado = false
            )

            when (val result = repository.registrarMascota(mascota)) {
                is Result.Success -> {
                    message = result.message
                    limpiarFormulario()
                }
                is Result.Error -> message = result.message
            }
            loading = false
        }
    }

    fun sincronizarPendientes() {
        viewModelScope.launch {
            repository.sincronizarPendientes()
        }
    }

    fun clearMessage() {
        message = null
    }

    private fun validarCampos(): String? {
        if (formState.nombre.isBlank() || formState.raza.isBlank() || formState.edad.isBlank() ||
            formState.sexo.isBlank() || formState.descripcion.isBlank() || formState.estado.isBlank()
        ) {
            return "Completa todos los campos obligatorios"
        }

        val edadInt = formState.edad.toIntOrNull()
        if (edadInt == null || edadInt < 0) {
            return "La edad debe ser un número válido"
        }

        return null
    }

    private fun limpiarFormulario() {
        formState = MascotaFormState()
    }
}
