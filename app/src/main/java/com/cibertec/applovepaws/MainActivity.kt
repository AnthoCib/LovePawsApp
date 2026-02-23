package com.cibertec.applovepaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import com.cibertec.applovepaws.feature_adopcion.ui.SolicitudScreen
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.core.theme.AppLovePawsTheme
import com.cibertec.applovepaws.feature_login.ui.LoginScreen
import com.cibertec.applovepaws.feature_login.ui.RegisterScreen
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen

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
                   var pantalla by remember { mutableStateOf("login") }

                    when (pantalla) {
                        "login" -> LoginScreen(
                            onIrARegistro = { pantalla = "register" }
                        )
                        "register" -> RegisterScreen(
                            onRegisterSuccess = { pantalla = "login" },
                            onCancelar = { pantalla = "login" }
                        )
                    }
                    //MascotaScreen()
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