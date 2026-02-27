package com.cibertec.applovepaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.core.theme.AppLovePawsTheme
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import com.cibertec.applovepaws.feature_adopcion.ui.SolicitudScreen
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.ui.MascotaDetalleScreen
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppLovePawsTheme {
                Surface {

                    // Estado: lista ↔ detalle
                    val mascotaSeleccionada = remember { mutableStateOf<MascotaDto?>(null) }

                    // Estado: abrir formulario
                    val mascotaIdFormulario = remember { mutableStateOf<Int?>(null) }
                    val mascotaNombreFormulario = remember { mutableStateOf("") }

                    // (TEMP) Para pruebas locales (luego vendrá del login real)
                    val usuarioIdPrueba = 1
                    val solicitanteNombrePrueba = ""
                    val celularPrueba = ""
                    val correoPrueba = ""

                    // ViewModel “a la simple”
                    val repoAdop = remember { AdopcionRepository(RetrofitClient.adoptionApi) }
                    val solicitudVM = remember { SolicitudViewModel(repoAdop) }

                    when {
                        // Formulario
                        mascotaIdFormulario.value != null -> {
                            val id = mascotaIdFormulario.value!!

                            SolicitudScreen(
                                viewModel = solicitudVM,
                                usuarioId = usuarioIdPrueba,
                                mascotaId = id,
                                mascotaNombre = mascotaNombreFormulario.value,
                                solicitanteNombre = solicitanteNombrePrueba,
                                celular = celularPrueba,
                                correo = correoPrueba,
                                onVolver = {
                                    mascotaIdFormulario.value = null
                                    mascotaNombreFormulario.value = ""
                                },
                                onIrHome = {
                                    // ✅ vuelve al home (lista)
                                    mascotaIdFormulario.value = null
                                    mascotaNombreFormulario.value = ""
                                    mascotaSeleccionada.value = null
                                }
                            )
                        }

                        // Lista
                        mascotaSeleccionada.value == null -> {
                            MascotaScreen(
                                onSeleccionarMascota = { mascota ->
                                    mascotaSeleccionada.value = mascota
                                }
                            )
                        }

                        // Detalle
                        else -> {
                            val mascota = mascotaSeleccionada.value!!

                            MascotaDetalleScreen(
                                mascota = mascota,
                                onVolver = { mascotaSeleccionada.value = null },
                                onDeseoAdoptar = { mascotaId ->
                                    mascotaIdFormulario.value = mascotaId
                                    mascotaNombreFormulario.value = mascota.nombre
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}