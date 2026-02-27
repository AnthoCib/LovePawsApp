package com.cibertec.applovepaws.feature_mascota

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.core.session.SessionManager
import com.cibertec.applovepaws.feature_mascota.data.AppDataBase
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.data.entity.MascotaEntity
import com.cibertec.applovepaws.feature_mascota.data.repository.MascotaRepository
import kotlinx.coroutines.launch

class MascotaViewModel(context: Context) : ViewModel() {

    private val dao = AppDataBase.getInstance(context).mascotaDao()
    private val repo = MascotaRepository(context, dao)
    private val esGestorSesion = SessionManager.esGestor(context)

    var mascotas by mutableStateOf<List<MascotaDto>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var registroExitoso by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
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

    fun cargarMascotasLocales() {
        viewModelScope.launch {
            loading = true
            errorMessage = null
            successMessage = null
            try {
                repo.sincronizarPendientes()
                mascotas = repo.obtenerMascotasLocales()
            } catch (e: Exception) {
                Log.e("DB_ERROR", e.message ?: "Error")
                errorMessage = "No se pudieron cargar mascotas locales"
            }
            loading = false
        }
    }

    fun puedeRegistrarMascotas(): Boolean = esGestorSesion

    fun sincronizarPendientes() {
        viewModelScope.launch {
            try {
                repo.sincronizarPendientes()
                mascotas = repo.obtenerMascotasLocales()
            } catch (e: Exception) {
                Log.e("SYNC_ERROR", e.message ?: "Error")
            }
        }
    }

    fun registrarMascota(
        nombre: String,
        razaId: Int,
        categoriaId: Int,
        edad: Int,
        sexo: String,
        descripcion: String?,
        fotoUrl: String?,
        estadoId: String?
    ) {
        viewModelScope.launch {
            if (!esGestorSesion) {
                successMessage = null
                errorMessage = "Solo el gestor puede registrar mascotas"
                return@launch
            }

            loading = true
            errorMessage = null
            successMessage = null
            try {
                val resultadoRegistro = repo.registrarMascota(
                    MascotaEntity(
                        nombre = nombre,
                        razaId = razaId,
                        categoriaId = categoriaId,
                        edad = edad,
                        sexo = sexo,
                        descripcion = descripcion,
                        fotoUrl = fotoUrl,
                        estadoId = estadoId
                    )
                )
                mascotas = repo.obtenerMascotasLocales()
                successMessage = resultadoRegistro
                registroExitoso = true
            } catch (e: Exception) {
                errorMessage = "Error al registrar mascota"
            }
            loading = false
        }
    }
}

class MascotaViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MascotaViewModel(context) as T
    }
}
