package com.cibertec.applovepaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import com.cibertec.applovepaws.feature_adopcion.ui.SolicitudScreen
import com.cibertec.applovepaws.feature_home.ui.HomeScreen
import com.cibertec.applovepaws.feature_login.LoginViewModelFactory
import com.cibertec.applovepaws.feature_login.ui.LoginScreen
import com.cibertec.applovepaws.feature_login.ui.RegisterScreen
import com.cibertec.applovepaws.feature_mascota.MascotaViewModelFactory
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.ui.MascotaDetalleScreen
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen
import com.cibertec.applovepaws.feature_mascota.ui.RegisterMascotaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface {
                    val repo = remember { AdopcionRepository(RetrofitClient.adoptionApi) }
                    val solicitudViewModel = remember { SolicitudViewModel(repo) }

                    var pantalla by remember { mutableStateOf("home") }
                    var reloadHome by remember { mutableStateOf(0) }
                    var reloadLogin by remember { mutableStateOf(0) }
                    var mascotaSeleccionada by remember { mutableStateOf<MascotaDto?>(null) }

                    // ID temporal para flujo cliente (backend actual no retorna usuarioId en login)
                    val usuarioId = 1010

                    when (pantalla) {
                        "home" -> key(reloadHome) {
                            HomeScreen(
                                onIrACatalogo = { pantalla = "catalogo" },
                                onIrALogin = { pantalla = "login"; reloadLogin++ },
                                onIrARegistro = { pantalla = "register" },
                                onCerrarSesion = { reloadHome++; reloadLogin++ }
                            )
                        }

                        "login" -> key(reloadLogin) {
                            LoginScreen(
                                viewModel = viewModel(factory = LoginViewModelFactory(applicationContext)),
                                onIrARegistro = { pantalla = "register" },
                                onLoginSuccess = { pantalla = "home"; reloadHome++ },
                                onVolver = { pantalla = "home"; reloadHome++ }
                            )
                        }

                        "register" -> RegisterScreen(
                            onRegisterSuccess = { pantalla = "login" },
                            onCancelar = { pantalla = "login" }
                        )

                        "catalogo" -> MascotaScreen(
                            viewModel = viewModel(factory = MascotaViewModelFactory(applicationContext)),
                            onIrARegistro = { pantalla = "registroMascota" },
                            onSeleccionarMascota = {
                                mascotaSeleccionada = it
                                pantalla = "detalleMascota"
                            }
                        )

                        "detalleMascota" -> {
                            val mascota = mascotaSeleccionada
                            if (mascota == null) {
                                pantalla = "catalogo"
                            } else {
                                MascotaDetalleScreen(
                                    mascota = mascota,
                                    onVolver = { pantalla = "catalogo" },
                                    onAdoptar = {
                                        solicitudViewModel.limpiarMensaje()
                                        pantalla = "solicitudAdopcion"
                                    }
                                )
                            }
                        }

                        "solicitudAdopcion" -> {
                            val mascota = mascotaSeleccionada
                            if (mascota == null) {
                                pantalla = "catalogo"
                            } else {
                                SolicitudScreen(
                                    viewModel = solicitudViewModel,
                                    usuarioId = usuarioId,
                                    mascotaId = mascota.id,
                                    onVolver = { pantalla = "detalleMascota" },
                                    onSolicitudExitosa = { pantalla = "catalogo" }
                                )
                            }
                        }

                        "registroMascota" -> RegisterMascotaScreen(
                            onRegistroExitoso = { pantalla = "catalogo" },
                            onCancelar = { pantalla = "catalogo" }
                        )
                    }
                }
            }
        }
    }
}
