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
import java.net.URI

class MascotaViewModel(context: Context) : ViewModel() {

    enum class FuenteListado { API, ROOM }

    private val appContext = context.applicationContext
    private val dao = AppDataBase.getInstance(appContext).mascotaDao()
    private val repo = MascotaRepository(appContext, dao)

    private fun esGestorSesion(): Boolean = SessionManager.esGestor(appContext)

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

    var fuenteActual by mutableStateOf(FuenteListado.ROOM)
        private set

    fun cargarMascotasApi() {
        viewModelScope.launch {
            loading = true
            errorMessage = null
            successMessage = null
            fuenteActual = FuenteListado.API
            try {
                mascotas = repo.obtenerMascotas()
                successMessage = "Listado cargado desde API Rest"
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Error")
                errorMessage = "No se pudo cargar listado desde API"
            }
            loading = false
        }
    }

    fun cargarMascotasRoom() {
        viewModelScope.launch {
            loading = true
            errorMessage = null
            successMessage = null
            fuenteActual = FuenteListado.ROOM
            try {
                val totalSincronizadas = repo.sincronizarPendientes()
                mascotas = repo.obtenerMascotasLocales()
                successMessage = if (totalSincronizadas > 0) {
                    "Listado ROOM cargado. Se sincronizaron $totalSincronizadas pendientes"
                } else {
                    "Listado cargado desde Room local"
                }
            } catch (e: Exception) {
                Log.e("DB_ERROR", e.message ?: "Error")
                errorMessage = "No se pudieron cargar mascotas desde Room"
            }
            loading = false
        }
    }

    fun alternarFuenteListado() {
        if (fuenteActual == FuenteListado.ROOM) {
            cargarMascotasApi()
        } else {
            cargarMascotasRoom()
        }
    }

    fun puedeRegistrarMascotas(): Boolean = esGestorSesion()

    fun sincronizarPendientes() {
        viewModelScope.launch {
            errorMessage = null
            try {
                val totalSincronizadas = repo.sincronizarPendientes()
                if (fuenteActual == FuenteListado.ROOM) {
                    mascotas = repo.obtenerMascotasLocales()
                }
                successMessage = if (totalSincronizadas > 0) {
                    "Sincronización manual: $totalSincronizadas mascotas"
                } else {
                    "No hay mascotas pendientes por sincronizar"
                }
            } catch (e: Exception) {
                Log.e("SYNC_ERROR", e.message ?: "Error")
                errorMessage = "Error al sincronizar pendientes"
            }
        }
    }

    private fun normalizarFotoUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        val limpia = url.trim()
        val conEsquema = if (limpia.startsWith("http://") || limpia.startsWith("https://")) limpia else "https://$limpia"
        return try {
            val uri = URI(conEsquema)
            if (uri.host.isNullOrBlank()) null else conEsquema
        } catch (_: Exception) {
            null
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
            if (!esGestorSesion()) {
                successMessage = null
                errorMessage = "Solo el gestor puede registrar mascotas"
                return@launch
            }

            loading = true
            errorMessage = null
            successMessage = null
            try {
                val fotoUrlNormalizada = normalizarFotoUrl(fotoUrl)
                if (fotoUrl != null && fotoUrl.isNotBlank() && fotoUrlNormalizada == null) {
                    errorMessage = "URL de imagen inválida"
                    loading = false
                    return@launch
                }

                val resultadoRegistro = repo.registrarMascota(
                    MascotaEntity(
                        nombre = nombre,
                        razaId = razaId,
                        categoriaId = categoriaId,
                        edad = edad,
                        sexo = sexo,
                        descripcion = descripcion,
                        fotoUrl = fotoUrlNormalizada,
                        estadoId = estadoId
                    )
                )
                if (fuenteActual == FuenteListado.ROOM) {
                    mascotas = repo.obtenerMascotasLocales()
                }
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
