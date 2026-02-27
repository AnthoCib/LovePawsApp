package com.cibertec.applovepaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.applovepaws.core.session.SessionManager
import com.cibertec.applovepaws.core.theme.AppLovePawsTheme
import com.cibertec.applovepaws.feature_home.ui.HomeScreen
import com.cibertec.applovepaws.feature_login.LoginViewModelFactory
import com.cibertec.applovepaws.feature_login.ui.LoginScreen
import com.cibertec.applovepaws.feature_login.ui.RegisterScreen
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.MascotaViewModelFactory
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen
import com.cibertec.applovepaws.feature_mascota.ui.RegisterMascotaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    var pantalla by remember { mutableStateOf("home") }
                    var reloadHome by remember { mutableStateOf(0) }
                    var reloadLogin by remember { mutableStateOf(0) }

                    val esGestor = SessionManager.esGestor(applicationContext)
                    val estaLogueado = SessionManager.estaLogueado(applicationContext)
                    when (pantalla) {
                        "home" -> key(reloadHome) {
                            HomeScreen(
                                onIrACatalogo = { pantalla = "catalogo" },
                                onIrALogin = { pantalla = "login"; reloadLogin++ },
                                onIrARegistro = { pantalla = "register" },
                                onCerrarSesion = {
                                    SessionManager.cerrarSesion(applicationContext)
                                    reloadHome++
                                    reloadLogin++
                                }
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
                            onCancelar = { pantalla = "login" },
                            onIrAHome = { pantalla = "home"; reloadHome++ }
                        )

                        "catalogo" -> {
                            val mascotaViewModel = viewModel<MascotaViewModel>(factory = MascotaViewModelFactory(applicationContext))
                            MascotaScreen(
                                viewModel = mascotaViewModel,
                                esGestor = esGestor,
                                mensajeRol = if (!estaLogueado) {
                                    "No logueado: solo visualización de mascotas."
                                } else if (esGestor) {
                                    "Rol GESTOR validado: puedes añadir y sincronizar mascotas."
                                } else {
                                    "Rol ADOPTANTE validado: solo puedes visualizar mascotas."
                                },
                                onIrARegistro = {
                                    if (esGestor) {
                                        pantalla = "registroMascota"
                                    }
                                },
                                onIrAHome = { pantalla = "home"; reloadHome++ },
                                onIrALogin = { pantalla = "login"; reloadLogin++ },
                                onSincronizar = {
                                    if (esGestor) {
                                        mascotaViewModel.sincronizarPendientes()
                                    }
                                }
                            )
                        }

                        "registroMascota" -> {
                            if (esGestor) {
                                RegisterMascotaScreen(
                                    onRegistroExitoso = { pantalla = "catalogo" },
                                    onCancelar = { pantalla = "catalogo" },
                                    onIrAHome = { pantalla = "home"; reloadHome++ }
                                )
                            } else {
                                pantalla = "catalogo"
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppLovePawsTheme {
        Greeting("Android")
    }
}
