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
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.core.theme.AppLovePawsTheme
import com.cibertec.applovepaws.feature_login.LoginViewModelFactory
import com.cibertec.applovepaws.feature_login.ui.LoginScreen
import com.cibertec.applovepaws.feature_login.ui.RegisterScreen
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.applovepaws.feature_home.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Instanciar el repositorio con Retrofit
        val repo = AdopcionRepository(RetrofitClient.adoptionApi)
        val viewModel = SolicitudViewModel(repo)

        // Usuario y Mascota de ejemplo (en producción vendrían de login y lista de mascotas)
        val usuarioId = 1010
        val mascotaId = 6001

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {

                    // Pantalla de Solicitud de Adopción
                   /** SolicitudScreen(
                        viewModel = viewModel,
                        usuarioId = usuarioId,
                        mascotaId = mascotaId
                    )*/
                   var pantalla by remember { mutableStateOf("home") }
                    var reloadHome by remember { mutableStateOf(0) }
                    var reloadLogin by remember { mutableStateOf(0) }

                    when (pantalla) {
                        "home" -> key (reloadHome) {   // ← fuerza recomposición cuando homeKey cambia
                            HomeScreen(
                                onIrACatalogo  = { pantalla = "catalogo" },
                                onIrALogin     = { pantalla = "login"; reloadLogin++ },
                                onIrARegistro  = { pantalla = "register" },
                                onCerrarSesion = { reloadHome++; reloadLogin++}
                            )
                        }
                        "login" -> key(reloadLogin) {  // ← envuelve LoginScreen con key
                            LoginScreen(
                                viewModel      = viewModel(factory = LoginViewModelFactory(applicationContext)),
                                onIrARegistro  = { pantalla = "register" },
                                onLoginSuccess = { pantalla = "home"; reloadHome++ },
                                onVolver       = { pantalla = "home"; reloadHome++ }
                            )
                        }
                        "register" -> RegisterScreen(
                            onRegisterSuccess = { pantalla = "login" },
                            onCancelar        = { pantalla = "login" }
                        )
                        "catalogo" -> MascotaScreen()
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